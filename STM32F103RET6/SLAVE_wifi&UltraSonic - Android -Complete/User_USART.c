#include "main.h"

void USART_Configuration(USART_TypeDef* USARTx, uint32_t BaudRate)
{
  USART_InitTypeDef USART_InitStructure;

  USART_InitStructure.USART_BaudRate = BaudRate;
  USART_InitStructure.USART_WordLength = USART_WordLength_8b;
  USART_InitStructure.USART_StopBits = USART_StopBits_1;
  USART_InitStructure.USART_Parity = USART_Parity_No;
  USART_InitStructure.USART_HardwareFlowControl = USART_HardwareFlowControl_None;
  USART_InitStructure.USART_Mode = USART_Mode_Rx | USART_Mode_Tx;

  /* Configure USART1 */
  USART_Init(USARTx, &USART_InitStructure);
  
  /* Enable USART1 Received interrupts */
  USART_ITConfig(USARTx, USART_IT_RXNE, ENABLE);
  
  /* Enable the USART1 */
  USART_Cmd(USARTx, ENABLE);
}


PUTCHAR_PROTOTYPE
{
    /* Write a character to the USART */  
    if( ch == '\n') {
        USART_SendData(UART4, '\r');
        while(USART_GetFlagStatus(UART4, USART_FLAG_TXE) == RESET);
        USART_SendData(UART4, '\n');
    }else {
        USART_SendData(UART4, (u8) ch);
    }

    /* Loop until the end of transmission */
    while(USART_GetFlagStatus(UART4, USART_FLAG_TXE) == RESET);

    return ch;
}

uint8_t USART_GetCharacter(USART_TypeDef *  usart_p)
{
    uint8_t data;

    /* Loop until the end of transmission */
    while(USART_GetFlagStatus(usart_p, USART_FLAG_RXNE) == RESET);

    /* Write a character to the USART */
    data = USART_ReceiveData(usart_p);

    USART_SendData(usart_p, data);
    while(USART_GetFlagStatus(usart_p, USART_FLAG_TXE) == RESET);

    if( data == '\r' )  return (int)('\n');
    else                return(data);
}

//String Type
void Tx_String(USART_TypeDef* USARTx, uint8_t *s)
{
  while(*s != '\0')
  {
    Tx_Data(USARTx, *s);
    s++;
  }
}     

//sign Type
void Tx_Sign(USART_TypeDef* USARTx, int16_t *pValue)
{
	if(*pValue>=0)
		Tx_Data(USARTx, '+');
	else if(*pValue<0){
		Tx_Data(USARTx, '-');
		*pValue *= (-1);
	}
}

//int Type 
void Tx_Dec(USART_TypeDef* USARTx, int16_t dec)
{
	char String[5]; //int type max size = 32768
	int  loop;
	
        Tx_Sign(USARTx, &dec);
                
	for (loop = 0 ; loop <5 ; loop++)
	{
		String[loop] = 0x30 + (dec % 10); 
		dec = dec / 10; 
	}
	for(loop = 4; loop >= 0; loop --)
		Tx_Data(USARTx, String[loop]); 
}

//int Type 
void Tx_uDec(USART_TypeDef* USARTx, uint16_t dec)
{
	char String[5]; //int type max size = 32768
	int  loop;
	      
	for (loop = 0 ; loop <5 ; loop++)
	{
		String[loop] = 0x30 + (dec % 10); 
		dec = dec / 10; 
	}
	for(loop = 4; loop >= 0; loop --)
		Tx_Data(USARTx, String[loop]); 
}

void Tx_Data(USART_TypeDef* USARTx, uint8_t tx_data)
{
  USART_SendData(USARTx, tx_data);// alreay defined in "stm32f10x.h" 
  while(USART_GetFlagStatus(USARTx, USART_FLAG_TXE) == RESET);
}


void Tx_Data16t(USART_TypeDef* USARTx, uint16_t tx_data)
{
  Tx_Data(USARTx, (uint8_t)((0xff00 & tx_data) >> 8));
  Tx_Data(USARTx, (uint8_t)(0x00ff & (tx_data)));
}

void EnQueue_Buffer(int8_t* pBufferArray, uint8_t rx_data, uint8_t BufferSize)
{  
  for( int idx = 1; idx < BufferSize; idx++)
  {
      pBufferArray[idx - 1]  = pBufferArray[idx];
  }
  pBufferArray[BufferSize - 1]  = rx_data;
}