#include "main.h"

void USART1_Configuration(void)
{
  USART_InitTypeDef USART_InitStructure;

  USART_InitStructure.USART_BaudRate = 230400;
  USART_InitStructure.USART_WordLength = USART_WordLength_8b;
  USART_InitStructure.USART_StopBits = USART_StopBits_1;
  USART_InitStructure.USART_Parity = USART_Parity_No;
  USART_InitStructure.USART_HardwareFlowControl = USART_HardwareFlowControl_None;
  USART_InitStructure.USART_Mode = USART_Mode_Rx | USART_Mode_Tx;

  /* Configure USART1 */
  USART_Init(USART1, &USART_InitStructure);
  
  /* Enable USART1 Transmit interrupts */
  USART_ITConfig(USART1, USART_IT_RXNE, ENABLE);
  
  /* Enable the USART1 */
  USART_Cmd(USART1, ENABLE);
}


void USART3_Configuration(void)
{
  USART_InitTypeDef USART_InitStructure;

  USART_InitStructure.USART_BaudRate = 9600;
  USART_InitStructure.USART_WordLength = USART_WordLength_8b;
  USART_InitStructure.USART_StopBits = USART_StopBits_1;
  USART_InitStructure.USART_Parity = USART_Parity_No;
  USART_InitStructure.USART_HardwareFlowControl = USART_HardwareFlowControl_None;
  USART_InitStructure.USART_Mode = USART_Mode_Rx | USART_Mode_Tx;

  /* Configure USART3 */
  USART_Init(USART3, &USART_InitStructure); 
  
  /* Enable USART3 Transmit interrupts */
  USART_ITConfig(USART3, USART_IT_RXNE, ENABLE);
  
  /* Enable the USART3 */
  USART_Cmd(USART3, ENABLE);
}

PUTCHAR_PROTOTYPE
{
    /* Write a character to the USART */  
    if( ch == '\n') {
        USART_SendData(USART3, '\r');
        while(USART_GetFlagStatus(USART3, USART_FLAG_TXE) == RESET);
        USART_SendData(USART3, '\n');
    }else {
        USART_SendData(USART3, (u8) ch);
    }

    /* Loop until the end of transmission */
    while(USART_GetFlagStatus(USART3, USART_FLAG_TXE) == RESET);

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
void usart3_tx_String(unsigned char *s)
{
  while(*s != '\0')
  {
    usart3_tx_data(*s);
    s++;
  }
}     

//sign Type
void usart3_tx_sign(int *pValue)
{
	if(*pValue>=0)
		usart3_tx_data('+');
	else if(*pValue<0){
		usart3_tx_data('-');
		*pValue *= (-1);
	}
}
//int Type 
void usart3_tx_dec(int dec)
{
	char String[5]; //int type max size = 32768
	int  loop;
	usart3_tx_sign(&dec); 
	for (loop = 0 ; loop <5 ; loop++)
	{
		String[loop] = 0x30 + (dec % 10); 
		dec = dec / 10; 
	}
	for(loop = 4; loop >= 0; loop --)
		usart3_tx_data(String[loop]); 
}

void usart1_tx_data(uint8_t tx_data)
{
  USART_SendData(USART1, tx_data);// alreay defined in "stm32f10x.h" 
  while(USART_GetFlagStatus(USART1, USART_FLAG_TXE) == RESET);
}

void usart3_tx_data(uint8_t tx_data)
{
  USART_SendData(USART3, tx_data);// alreay defined in "stm32f10x.h" 
  while(USART_GetFlagStatus(USART3, USART_FLAG_TXE) == RESET);
}