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

extern __IO uint32_t IC1Value;
extern __IO uint32_t IC2Value;

#define INITIALIZE   0
#define ROLL         0
#define PITCH        1
#define YAW          2
#define HEIGHT       3
typedef struct __axis
{
  __IO  int16_t Current;
  __IO  int16_t Target;
  __IO  double KP;
  __IO  double KI;
  __IO  double KD;
  __IO  int16_t PID_value;
}__axis;
typedef struct __Drone_Control
{
  __axis axis[4];
  __IO  uint16_t Throttle;
  __IO  uint16_t Height1;
  __IO  uint16_t Height2;
  __IO  uint8_t   Send_on;
}__Drone_Control;


/*******************************************************************************
                            General_Configuration
*******************************************************************************/


void RCC_Configuration(void);
void GPIO_Configuration(void);
void NVIC_Configuration(void);

/*******************************************************************************
                            TIMER_Configuration
*******************************************************************************/

#define Oscillator 5000000
#define PREIOD 65535
#define PRESCALER 32
#define dt 0.003

void TIM_Configuration(void);
void delay_us(int cnt);
void delay_ms(int cnt);

/*******************************************************************************
                            USART_Configuration
*******************************************************************************/

#define MAIN_BUFFER_SIZE 10
#define WIFI_BUFFER_SIZE 40
#define GPS_BUFFER_SIZE  30
#define BUFFER_CHECK(pBuffer) ( ( ((0xFF00)&((pBuffer[0])<<8)) | ((0x00FF)&(pBuffer[1])) ) == (0x5555) ) ? true : false
#define BYTE_MERGE(pBuff,idx) ( (0xFF00) & (pBuff[idx]<<8) ) | ( (0x00FF) & (pBuff[idx+1] ) )


void USART_Configuration(USART_TypeDef* USARTx, uint32_t BaudRate);

void Tx_String(USART_TypeDef* USARTx, uint8_t *s);
void Tx_Sign(USART_TypeDef* USARTx, int16_t *pValue);
void Tx_Dec(USART_TypeDef* USARTx, int16_t dec);
void Tx_uDec(USART_TypeDef* USARTx, uint16_t dec);
void Tx_Data(USART_TypeDef* USARTx, uint8_t tx_data);

uint8_t USART_GetCharacter(USART_TypeDef *  usart_p);

void Tx_Data16t(USART_TypeDef* USARTx, uint16_t tx_data);
void EnQueue_Buffer(int8_t* pBufferArray, uint8_t rx_data, uint8_t BufferSize);


/******************************************************************************/