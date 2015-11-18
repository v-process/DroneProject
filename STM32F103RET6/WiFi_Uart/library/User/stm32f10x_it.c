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
  * @brief  This function hhandles NMI exception.
  * @param  None
  * @retval None
  */

uint16_t  RxBuffer[10];
int16_t  roll_TARGET_ANGLE = 0, pitch_TARGET_ANGLE = 0, yaw_TARGET_ANGLE = 0;
int16_t  roll_kp = 0, pitch_kp = 0, yaw_kp = 0,
         roll_ki = 0, pitch_ki = 0, yaw_ki = 0,
         roll_kd = 0, pitch_kd = 0, yaw_kd = 0;
int16_t  chk = 0, sum = 0, 
         roll = 0, pitch = 0, yaw = 0,
         roll_err = 0, pitch_err = 0, yaw_err = 0,
         roll_err_ary[5] = {0}, pitch_err_ary[5] = {0}, yaw_err_ary[5] = {0},
         roll_err_sum = 0, pitch_err_sum = 0, yaw_err_sum = 0,
         roll_p_ctrl_val = 0, pitch_p_ctrl_val = 0, yaw_p_ctrl_val = 0,
         roll_i_ctrl_val = 0, pitch_i_ctrl_val = 0, yaw_i_ctrl_val = 0,
         roll_d_ctrl_val = 0, pitch_d_ctrl_val = 0, yaw_d_ctrl_val = 0,
         PID_roll = 0, PID_pitch = 0, PID_yaw = 0;

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

/**
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

void TIM2_IRQHandler(void)
{
  if (TIM_GetITStatus(TIM2, TIM_IT_CC1) != RESET)
  {
    /* Clear TIM2 Capture Compare1 interrupt pending bit*/
    TIM_ClearITPendingBit(TIM2, TIM_IT_CC1);
  }  
}

void USART3_IRQHandler(void)
{
  uint8_t  wifi_rx_data;
    
  if(USART_GetITStatus(USART3, USART_IT_RXNE) != RESET)
  { 
    USART_ClearITPendingBit(USART3, USART_IT_RXNE);
    
    wifi_rx_data = USART_ReceiveData(USART3); 
    
    if(wifi_rx_data == '*') {
      printf("AT+CIPSEND=0,6\n\r");   // Destination ID is '0'. - 6 byte sending
      delay_ms(100);
      printf("Banana\n\r"); 
      delay_ms(100);      
    }
    
    /*if(wifi_rx_data == '1') {
      roll_TARGET_ANGLE = roll;
      pitch_TARGET_ANGLE = pitch;
      yaw_TARGET_ANGLE = yaw;
      
      printf("%d %d %d\n", roll, pitch, yaw);
    }
    else if(wifi_rx_data == '2') {
      printf("%d %d %d\n", PID_roll, PID_pitch, PID_yaw);
    }*/
  }
}

void USART1_IRQHandler(void)  
{
  uint8_t i, buffer;
  
  if(USART_GetITStatus(USART1, USART_IT_RXNE) != RESET)
  {  
    USART_ClearITPendingBit(USART1, USART_IT_RXNE);
    
    buffer = USART_ReceiveData(USART1); 
    RxBuffer[0]  = RxBuffer[1];
    RxBuffer[1]  = RxBuffer[2];
    RxBuffer[2]  = RxBuffer[3];
    RxBuffer[3]  = RxBuffer[4];
    RxBuffer[4]  = RxBuffer[5];
    RxBuffer[5]  = RxBuffer[6];
    RxBuffer[6]  = RxBuffer[7];
    RxBuffer[7]  = RxBuffer[8];
    RxBuffer[8]  = RxBuffer[9];
    RxBuffer[9]  = buffer;

    //printf("%d ", a++);
    //printf("%X ", buffer);
    
    if(RxBuffer[0] == 0x55 && RxBuffer[1] == 0x55)
    {
      NVIC_DisableIRQ(USART1_IRQn);
      //GPIO_SetBits(GPIOB, GPIO_Pin_0);
        
      chk = ((0xFF00)&(RxBuffer[8]<<8))|(0x00FF&RxBuffer[9]);
      for(i = 0; i < 8 ; i++) sum += RxBuffer[i];
      
      if(chk == sum) {
        roll = ((0xFF00)&(RxBuffer[2]<<8))|(0x00FF&RxBuffer[3]);
        pitch = ((0xFF00)&(RxBuffer[4]<<8))|(0x00FF&RxBuffer[5]);
        yaw = ((0xFF00)&(RxBuffer[6]<<8))|(0x00FF&RxBuffer[7]);
      }
      //GPIO_ResetBits(GPIOB, GPIO_Pin_0);
        
      roll_err = roll_TARGET_ANGLE - roll;
      pitch_err = pitch_TARGET_ANGLE - pitch;
      yaw_err = yaw_TARGET_ANGLE - yaw;
      for(i = 0; i < 5; i++) {
        roll_err_sum += roll_err_ary[i];
        pitch_err_sum += pitch_err_ary[i];
        yaw_err_sum += yaw_err_ary[i];
      }
      
      roll_p_ctrl_val = roll_kp * roll_err;
      pitch_p_ctrl_val = pitch_kp * pitch_err;
      yaw_p_ctrl_val = yaw_kp * yaw_err;
      
      roll_i_ctrl_val = roll_ki * roll_err_sum;
      pitch_i_ctrl_val = pitch_ki * pitch_err_sum;
      yaw_i_ctrl_val = yaw_ki * yaw_err_sum;
      
      roll_d_ctrl_val = roll_kd * (roll_err - roll_err_ary[4]);
      pitch_d_ctrl_val = pitch_kd * (pitch_err - pitch_err_ary[4]);
      yaw_d_ctrl_val = yaw_kd * (yaw_err - yaw_err_ary[4]);
      
      PID_roll = roll_p_ctrl_val + roll_i_ctrl_val + roll_d_ctrl_val;
      PID_pitch = pitch_p_ctrl_val + pitch_i_ctrl_val + pitch_d_ctrl_val;
      PID_yaw = yaw_p_ctrl_val + yaw_i_ctrl_val + yaw_d_ctrl_val;
       
      
      for(i = 0; i < 4; i++) {
        roll_err_ary[i] = roll_err_ary[i+1];
        pitch_err_ary[i] = pitch_err_ary[i+1];
        yaw_err_ary[i] = yaw_err_ary[i+1];
      }
      roll_err_ary[4] = roll_err; pitch_err_ary[4] = pitch_err; yaw_err_ary[4] = yaw_err;
      roll_err_sum = 0; pitch_err_sum = 0; yaw_err_sum = 0;
      sum = 0;
      
      NVIC_EnableIRQ(USART1_IRQn);
    }
  }  
}

/******************* (C) COPYRIGHT 2011 STMicroelectronics *****END OF FILE****/
