package com.example.asnaui.mobiledtr;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    @Test
    public void swap() throws Exception{
        int num1 = 5;
        int num2 = 9;

        num1 = num1 + num2; // 14
        num2 = num1 - num2; // 5
        num1 = num1 - num2; // 9

        assertEquals("9 5",num1+" "+num2);
    }
    @Test
    public void mulitplication() throws Exception {
        int total = 0;
        int num1 = 4;
        int num2 = 4;
        for (int i = 1; i <= num1; i++) {
            for (int j = 1; j <= num2; j++) {
                total++;
            }
        }
        assertEquals(16, total);
    }
    @Test
    public void reverse() throws Exception{
        String text = "hello";
        String reverse = "";
        for(int i=text.length()-1; i>-1; i--){
            reverse += text.charAt(i);
        }
        assertEquals("olleh",reverse);
    }
    @Test
    public void countVowels(){
        String text = "hello";
        int count = 0;
        for(int i=0; i<text.length(); i++){
            switch (text.toLowerCase().charAt(i)){
                case 'a':
                    count++;
                    break;
                case 'e':
                    count++;
                    break;
                case 'i':
                    count++;
                    break;
                case 'o':
                    count++;
                    break;
                case 'u':
                    count++;
                    break;
            }
        }
        assertEquals(2,count);
    }

}