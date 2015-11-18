#include "main.h"

void Delay(vu32 nCount)
{
  for(; nCount != 0; nCount--);
}

void initailze_motorSet(int value)
{ 
    TIM3->CCR1 = value;
    TIM3->CCR2 = value;

    TIM5->CCR1 = value;
    TIM5->CCR2 = value;
    TIM5->CCR3 = value;
    TIM5->CCR4 = value;
}


int main()
{  
  RCC_Configuration();
  NVIC_Configuration();
  GPIO_Configuration();
 
  USART1_Configuration(); 
  USART3_Configuration(); 
  TIM_Configuration();
  PWM_Configuration();
  
  initailze_motorSet(18000); // 30%
  
  Delay(0x1FFFFF);
  initailze_motorSet(0); 
  Delay(0x1FFFFF);

  for(int nCount = 18000; nCount < 30000; nCount++) // MAX : 20000 -> X, 30000~ -> O
  {
    initailze_motorSet(nCount); 
    Delay(0x1FF);
  } 
  
  while(1);
}