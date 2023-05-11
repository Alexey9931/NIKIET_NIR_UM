import java.net.ConnectException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.swing.JOptionPane;

import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;
import jssc.SerialPortList;
import jssc.SerialPortTimeoutException;

public class ComPort 
{
	private static SerialPort serialPort;
	public static String[] portNames;
	public static String CurrentPortName;
	public static int CurrentBaudRate = 115200;
	
	public static ArrayList<Integer> RX_BUF = new ArrayList<>();
	public static int RX_BUF_SIZE;
	
	public static void Connect()
	{
		//Передаём в конструктор имя порта
        serialPort = new SerialPort(CurrentPortName);
        try {
            //Открываем порт
            serialPort.openPort();
            //Выставляем параметры
            serialPort.setParams(CurrentBaudRate,
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE,false,false);
            serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);
            //Устанавливаем ивент лисенер и маску
            serialPort.addEventListener(new PortReader(), SerialPort.MASK_RXCHAR);
            GUI.StatusTextArea.setText(CurrentPortName+" connected!\n");
        }
        catch (SerialPortException ex) {
            System.out.println(ex);
            GUI.StatusTextArea.setText("Connection ERROR!\n");
        }
	}
	public static void SendData(byte data[])
	{
		GUI.RXTextArea.setText("");
		try 
		{
			System.out.println();
			System.out.print("TX: ");
			GUI.TXTextArea.setText("TX: ");
			GUI.RXTextArea.append("RX: ");
			for (int i = 0; i < data.length; i++)
			{
				serialPort.writeByte(data[i]);
				GUI.TXTextArea.append(String.format("%02X ", data[i]));
				System.out.print(String.format("%02X ", data[i]));
			}
			System.out.println();
			System.out.print("RX: ");
		}
		catch (SerialPortException ex) {
            System.out.println(ex);
            JOptionPane.showMessageDialog(null, "Ошибка отправки пакета данных! Проверьте наличие соединения!");
        }
		catch(Exception ex)
		{
			System.out.println(ex);
			JOptionPane.showMessageDialog(null, "Ошибка отправки пакета данных! Проверьте наличие соединения!");
		}
	}
	public static void Disconnect()
	{
		try 
		{
			serialPort.closePort();
			GUI.StatusTextArea.setText(CurrentPortName+" disconnected!\n");
		}
		catch (SerialPortException ex) {
            System.out.println(ex);
            GUI.StatusTextArea.setText("Disconnecting ERROR!\n");
        }
	}
	public static void ScanComPorts()
	{
		portNames = SerialPortList.getPortNames();
		String[] array = new String[portNames.length+1];
		array[0] = " ";
		for (int i = 0; i < portNames.length; i++)
		{
			array[i+1] = portNames[i];
		}
		portNames = array;
	}
	private static class PortReader implements SerialPortEventListener {
        public void serialEvent(SerialPortEvent event) {
        	if (event.isRXCHAR() && event.getEventValue() > 0) {
                try {
                    //Получаем ответ от устройства, обрабатываем данные и т.д.
                    String[] data = serialPort.readHexStringArray();                  
                    for (int i = 0; i < data.length; i++)
                    {
                    	RX_BUF.add(RX_BUF_SIZE, Integer.parseInt(data[i],16));
                    	GUI.RXTextArea.append(data[i]+" ");
                    	System.out.print(data[i]+" ");
                    	if ((RX_BUF.get(RX_BUF_SIZE) == 0xAA)&&(RX_BUF.get(RX_BUF_SIZE-1) == 0xAA))
                    	{
                    		RX_BUF_SIZE = 0;
                    		MODBUS.ModbusResponseData();
                    		//System.out.println(RX_BUF);
                    	}
                    	else
                    	{
                    		RX_BUF_SIZE++;
                    	}
                    }   
                    //System.out.println();
                    //System.out.println(String.join(" ",data));
                }
                catch (SerialPortException ex) {
                    System.out.println(ex);
                    GUI.RXTextArea.setText("Receive ERROR!\n");
                } 
        	}
        }
    }
}
