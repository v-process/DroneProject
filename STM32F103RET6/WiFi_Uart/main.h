#include "stm32f10x.h"

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdint.h>
#include <math.h>

#ifdef __GNUC__
  #define PUTCHAR_PROTOTYPE int __io_putchar(int ch)
#else
  #define PUTCHAR_PROTOTYPE int fputc(int ch, FILE *f)
#endif /* __GNUC__ */

#define INITIALIZE              0

//LED
#define GPIO_LED1_PIN	  	GPIO_Pin_0      //PB00  
#define GPIO_LED2_PIN           GPIO_Pin_1     
#define GPIO_LED3_PIN           GPIO_Pin_2     

//USART4 - Wifi Module
#define UART4_TX_PIN	  	GPIO_Pin_10     //PC10
#define UART4_RX_PIN            GPIO_Pin_11     //PC11
#define RxBufferSize            18

//USART4 - GPS Module
#define UART5_TX_PIN	  	GPIO_Pin_12     //PC12
#define UART5_RX_PIN            GPIO_Pin_2      //PD02


typedef struct motor_CCR
{
  uint16_t TIM3_CCR1;
  uint16_t TIM3_CCR2;
  
  uint16_t TIM5_CCR1;
  uint16_t TIM5_CCR2;
  uint16_t TIM5_CCR3;
  uint16_t TIM5_CCR4;
}Motor_CCR;

void RCC_Configuration(void);
void GPIO_Configuration(void);
void NVIC_Configuration(void);

void TIM_Configuration(void);
void PWM_Configuration(void);
void USART1_Configuration(void);
void USART2_Configuration(void);
void USART3_Configuration(void);
uint8_t USART_GetCharacter(USART_TypeDef *  usart_p);

void Delay_ms(__IO uint32_t nTime);
void Delay(__IO uint32_t nTime);
void TimingDelay_Decrement(void);                        

void usart3_tx_String(uint8_t *s);
void usart3_tx_dec(int dec);
void usart3_tx_sign(int *pValue);

void usart1_tx_data(uint8_t tx_data);
void usart2_tx_data(uint8_t tx_data);
void usart3_tx_data(uint8_t tx_data);

void wifi_tx_String(unsigned char *s);
void wifi_tx_dec(int dec);
void wifi_tx_data(uint8_t tx_data);

void WRITE_Motor_Data_Speed(uint8_t ID, uint16_t Pos, uint16_t Speed);