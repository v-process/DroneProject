/**
  ******************************************************************************
  * @file    GPIO/IOToggle/stm32f10x_it.c 
  * @author  MCD Application Team
  * @version V3.5.0
  * @date    08-April-2011
  * @brief   Main Interrupt Service Routines.
  *          This file provides template for all exceptions handler and peripherals
  *          interrupt service routine.
  ******************************************************************************
  * @attention
  *
  * THE PRESENT FIRMWARE WHICH IS FOR GUIDANCE ONLY AIMS AT PROVIDING CUSTOMERS
  * WITH CODING INFORMATION REGARDING THEIR PRODUCTS IN ORDER FOR THEM TO SAVE
  * TIME. AS A RESULT, STMICROELECTRONICS SHALL NOT BE HELD LIABLE FOR ANY
  * DIRECT, INDIRECT OR CONSEQUENTIAL DAMAGES WITH RESPECT TO ANY CLAIMS ARISING
  * FROM THE CONTENT OF SUCH FIRMWARE AND/OR THE USE MADE BY CUSTOMERS OF THE
  * CODING INFORMATION CONTAINED HEREIN IN CONNECTION WITH THEIR PRODUCTS.
  *
  * <h2><center>&copy; COPYRIGHT 2011 STMicroelectronics</center></h2>
  ******************************************************************************
  */

/* Includes ------------------------------------------------------------------*/
#include "stm32f10x_it.h"
#include "main.h"


void String_To_Dec2(int16_t __IO *x, int16_t __IO *y);
int16_t String_To_Dec();

enum { CONNECT = 0, OPEN, RECEIVE, CLOSED };

extern __IO __Drone_Control Drone_Control;
extern __IO uint8_t Wifi_Connect;
extern __IO uint8_t Send_Exit;

__IO uint16_t var = 0;
__IO uint32_t IC1Value = 0;
__IO uint16_t T = 0;

char Wifi_Buffer[40];
int8_t Main_Buffer[MAIN_BUFFER_SIZE];

uint8_t Wifi_State = 0;
uint8_t Wifi_Index = 0;
uint8_t WIfi_Check_flag = 0;

int16_t value[2] = {0,};
uint8_t num= 0;
int16_t index =0;
/** @addtogroup STM32F10x_StdPeriph_Examples
  * @{
  */

/** @addtogroup GPIO_IOToggle
  * @{
  */

/* Private typedef -----------------------------------------------------------*/
/* Private define ------------------------------------------------------------*/
/* Private macro -------------------------------------------------------------*/
/* Private variables ---------------------------------------------------------*/
/* Private function prototypes -----------------------------------------------*/
/* Private functions ---------------------------------------------------------*/

/******************************************************************************/
/*            Cortex-M3 Processor Exceptions Handlers                         */
/******************************************************************************/

/**
  * @brief  This function handles NMI exception.
  * @param  None
  * @retval None
  */
void NMI_Handler(void)
{
}

/**
  * @brief  This function handles Hard Fault exception.
  * @param  None
  * @retval None
  */
void HardFault_Handler(void)
{
  /* Go to infinite loop when Hard Fault exception occurs */
  while (1)
  {
  }
}

/**
  * @brief  This function handles Memory Manage exception.
  * @param  None
  * @retval None
  */
void MemManage_Handler(void)
{
  /* Go to infinite loop when Memory Manage exception occurs */
  while (1)
  {
  }
}

/**
  * @brief  This function handles Bus Fault exception.
  * @param  None
  * @retval None
  */
void BusFault_Handler(void)
{
  /* Go to infinite loop when Bus Fault exception occurs */
  while (1)
  {
  }
}

/**d
  * @brief  This function handles Usage Fault exception.
  * @param  None
  * @retval None
  */
void UsageFault_Handler(void)
{
  /* Go to infinite loop when Usage Fault exception occurs */
  while (1)
  {
  }
}

/**
  * @brief  This function handles SVCall exception.
  * @param  None
  * @retval None
  */
void SVC_Handler(void)
{
}

/**
  * @brief  This function handles Debug Monitor exception.
  * @param  None
  * @retval None
  */
void DebugMon_Handler(void)
{
}

/**
  * @brief  This function handles PendSV_Handler exception.
  * @param  None
  * @retval None
  */
void PendSV_Handler(void)
{
}

/**
  * @brief  This function handles SysTick Handler.
  * @param  None
  * @retval None
  */
void SysTick_Handler(void)
{
}


/******************************************************************************/
/*                 STM32F10x Peripherals Interrupt Handlers                   */
/*  Add here the Interrupt Handler for the used peripheral(s) (PPP), for the  */
/*  available peripheral interrupt handler's name please refer to the startup */
/*  file (startup_stm32f10x_xx.s).                                            */
/******************************************************************************/

void TIM3_IRQHandler(void)
{
 /* Clear TIM3 Capture compare interrupt pending bit */
  TIM_ClearITPendingBit(TIM3, TIM_IT_CC2);

  /* Get the Input Capture value */
  Drone_Control.Height = (uint16_t)TIM_GetCapture1(TIM3);
}

void TIM5_IRQHandler(void)
{
 /* Clear TIM3 Capture compare interrupt pending bit */
  TIM_ClearITPendingBit(TIM5, TIM_IT_CC2);

  /* Get the Input Capture value */
  Drone_Control.Height1 = (uint16_t)TIM_GetCapture1(TIM5);
}

void USART2_IRQHandler(void)
{
  uint8_t  data;
    
  if(USART_GetITStatus(USART2, USART_IT_RXNE) != RESET)
  {
    USART_ClearITPendingBit(USART2, USART_IT_RXNE);
    
    data = USART_ReceiveData(USART2);
   printf("%d", data);
  }
}

void USART3_IRQHandler(void)
{
  uint8_t rxdata = 0;
  if(USART_GetITStatus(USART3, USART_IT_RXNE) != RESET)
  { 
    USART_ClearITPendingBit(USART3, USART_IT_RXNE);
    rxdata = USART_ReceiveData(USART3);
    EnQueue_Buffer(Main_Buffer, rxdata, MAIN_BUFFER_SIZE);
        
    if(BUFFER_CHECK(Main_Buffer) && (Main_Buffer[MAIN_BUFFER_SIZE-2] == Main_Buffer[MAIN_BUFFER_SIZE-1] ==0xFF))
    {
      Drone_Control.axis[ROLL].angle  = BYTE_MERGE(Main_Buffer, 2);
      Drone_Control.axis[PITCH].angle = BYTE_MERGE(Main_Buffer, 4);
      Drone_Control.axis[YAW].angle   = BYTE_MERGE(Main_Buffer, 6);

    }
  }
}

void UART4_IRQHandler(void)
{
  //uint8_t  wifi_rx_data = 0;
  uint8_t  rx_data = 0;
  
  if(USART_GetITStatus(UART4, USART_IT_RXNE) != RESET)
  { 
    USART_ClearITPendingBit(UART4, USART_IT_RXNE);
    
    rx_data = USART_ReceiveData(UART4); 
   
    
    /*Wifi_CONECT Check*/
    if (Wifi_State == CONNECT)
    {   
      if (rx_data == 'C') {    
        WIfi_Check_flag = SET;
      }
      if (WIfi_Check_flag == SET) {
        Wifi_Buffer[Wifi_Index++] = rx_data;

        if (Wifi_Index > 6) {     
          if (!strncmp(Wifi_Buffer, "CONNECT", 7)) {
            Tx_String(USART1, "CONNECT\n");
            Wifi_Connect = 1;
            Wifi_State = OPEN;
            Wifi_Index = RESET;
            WIfi_Check_flag = RESET;
          } 
        } 
      } 
    }      
    
    /*Wifi_CONECT Complete*/  
    else if (Wifi_State == OPEN) { 
      if      (rx_data == '+') Wifi_State = RECEIVE; //receive_waiting
      else if (rx_data == '0') Wifi_State = CLOSED; //wifi_closed
    }
 
    /*Wifi_Receive Data check */
    else if (Wifi_State == RECEIVE) {  
      if (rx_data == ':') WIfi_Check_flag = SET;

      else if (WIfi_Check_flag == SET) {    
        Wifi_Buffer[Wifi_Index++] = rx_data;
    
        if (Wifi_Buffer[Wifi_Index - 2] == 'O' && Wifi_Buffer[Wifi_Index - 1] == 'K') {   
          for(int i = 0; i < Wifi_Index-3; i++) Tx_Data(USART1, Wifi_Buffer[i]);
          
          switch(Wifi_Buffer[0]) {
          case 'S':
            value[0] = value[1] = num = index = 0;
            
            for(int i = 0; i < Wifi_Index - 2; i++){
              if(Wifi_Buffer[i] == '*')
              {
                for (int j = i - 1, n = 0; j>index; j--, n++)
	        {
	          value[num] += (Wifi_Buffer[j]-'0') * pow((double)10, (int)n);
	        }
                index = i+1;
                num++;
              }        
            } 
            Drone_Control.axis[PITCH].TargetAngle = value[0];
            Drone_Control.axis[ROLL].TargetAngle = value[1];
            Tx_String(USART1, "\nA ");
            Tx_Dec(USART1, Drone_Control.axis[ROLL].TargetAngle);
            Tx_Dec(USART1, Drone_Control.axis[PITCH].TargetAngle);
            break;

          case 'P':
            Drone_Control.axis[PITCH].KP = String_To_Dec();
            Tx_String(USART1, "\nP ");
            Tx_Dec(USART1, Drone_Control.axis[PITCH].KP);
            break;
          case 'I':
            Drone_Control.axis[PITCH].KI = String_To_Dec();
            break;
          case 'D':
            Drone_Control.axis[PITCH].KD = String_To_Dec();
            break;
          case 'T':
            T = String_To_Dec();
            if(T == 100){   Drone_Control.Throttle = 2850; Send_Exit = 1;}
            else            Drone_Control.Throttle = T;
            break;
          }
          Wifi_State = OPEN;
          Wifi_Index = RESET;
          WIfi_Check_flag = RESET;
          delay_ms(2);
        } 
      }  
    }
    
    /*Wifi_CLOSED */
    else if (Wifi_State == CLOSED) {   
      if (rx_data == 'C'){
        WIfi_Check_flag = SET;
      }
 
      if (WIfi_Check_flag == SET) {    
        Wifi_Buffer[Wifi_Index++] = rx_data;
        if (Wifi_Index > 5) {     
          if (!strncmp(Wifi_Buffer, "CLOSED", 6)) {  
            Wifi_Index = RESET;
            Wifi_State = CONNECT;
            WIfi_Check_flag = RESET;
            Wifi_Connect = RESET;
          }  
        }
      }
    }
  }
}

/******************* (C) COPYRIGHT 2011 STMicroelectronics *****END OF FILE****/

int16_t String_To_Dec()
{
    int16_t value = 0;
    for(int i = 0; i < Wifi_Index - 2; i++){
        if(Wifi_Buffer[i] == '*')
        {
          for (int j = i - 1, n = 0; j>0; j--, n++)
	  {
	    value += (Wifi_Buffer[j]-'0') * pow((double)10, (int)n);
	  }
        }         
    }
    return value;
}

void String_To_Dec2(int16_t __IO *x, int16_t __IO *y)
{
  int16_t value[2] = {0,};
  int n= 0;
    for(int i = 0; i < Wifi_Index - 2; i++){
        if(Wifi_Buffer[i] == '*')
        {
          for (int j = i - 1, n = 0; j>0; j--, n++)
	  {
	    value[n] += (Wifi_Buffer[j]-'0') * pow((double)10, (int)n);
	  }
          n++;
        }        
    }
    *x = value[0];
    *y = value[1];
}
