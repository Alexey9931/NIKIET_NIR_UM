import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MODBUS 
{
	//public static String DATA = GUI.DataTextField.getText();
	public static int UmModAddr;
	public static int UmChassisAddr;
	public static int PmModAddr;
	public static int PmChassisAddr;
	public static byte TX_BUF[];
	
	public static void ModbusSendData(byte receiver_mod_addr, byte receiver_chassis_addr, byte sender_mod_addr, byte sender_chassis_addr, byte cmd, List<Byte> data, byte data_size, byte device)
	{
		TX_BUF = new byte[data_size+13]; //буфер для отправки
		byte header = 0x55; //заголовок
		byte address_receiver; //адрес получателя
		byte address_sender; //адрес отправителя
		short data_length; //длина пакета
		byte service_byte = 0; //сервисный байт
		int checksum; //контрольная сумма
		short end = (short) 0xAAAA; //признак конца пакета
		
		address_receiver = (byte) ((receiver_chassis_addr << 4) | receiver_mod_addr);
		address_sender = (byte) ((sender_chassis_addr << 4) | sender_mod_addr);
		data_length = (short) (10 + data_size);
		if (device == 0)
		{
			service_byte = 0x1; //пока взял такой - "все исправно, управл по шине 1"
		}
		else if (device == 1)
		{
			service_byte = (byte) 0x80; //пока взял такой - "все исправно, готов к управлению"
		}
		
		byte buf[] = new byte[5 + data_size];//буффер для расчета контрольной суммы
		buf[0] = address_sender;
		buf[1] = (byte) (data_length & 0xFF);
		buf[2] = (byte) (data_length >> 8);
		buf[3] = service_byte;
		buf[4] = cmd;
		//String DataInBytes = new String();
		TX_BUF[0] = header;
		TX_BUF[1] = address_receiver;
		TX_BUF[2] = address_sender;
		TX_BUF[3] = buf[1];
		TX_BUF[4] = buf[2];
		TX_BUF[5] = service_byte;
		TX_BUF[6] = cmd;
		for (int k = 0; k < data_size; k++)
		{
			buf[k+5] = data.get(k);
			TX_BUF[7+k] = data.get(k);
			//DataInBytes += String.format("0x%02x ", data.get(k));
		}
		checksum = (int) FindCrc(buf, 5 + data_size);
		TX_BUF[7+data_size] = (byte)(checksum);
		TX_BUF[8+data_size] = (byte)(checksum>>8);
		TX_BUF[9+data_size] = (byte)(checksum>>16);
		TX_BUF[10+data_size] = (byte)(checksum>>24);
		
		TX_BUF[11+data_size] = (byte)(end);
		TX_BUF[12+data_size] = (byte)(end>>8);
		
		ComPort.SendData(TX_BUF);
		//ComPort.SendData( String.format("0x%02x ", header) + String.format("0x%02x ", address_receiver) + String.format("0x%02x ", address_sender) + String.format("0x%02x ", buf[1])  + String.format("0x%02x ", buf[2]) + String.format("0x%02x ", service_byte) + String.format("0x%02x ", cmd) + DataInBytes + String.format("0x%02x ", (byte)(checksum>>24)) + String.format("0x%02x ", (byte)(checksum>>16)) + String.format("0x%02x ", (byte)(checksum>>8)) + String.format("0x%02x ", (byte)checksum) + String.format("0x%02x ", (byte)(end>>8)) + String.format("0x%02x ", (byte)end));
	}
	
	public static void ModbusResponseData()
	{
		int Header = ComPort.RX_BUF.get(0);
		int AddressReceiver = ComPort.RX_BUF.get(1);
		int AddressSender = ComPort.RX_BUF.get(2);
		int DataLength = ComPort.RX_BUF.get(3)|(ComPort.RX_BUF.get(4)<<8);
		int ServiceByte = ComPort.RX_BUF.get(5);
		int Cmd = ComPort.RX_BUF.get(6);
		int UsefulDataLength = ComPort.RX_BUF.size()-13;
		int UsefulData[] = new int[UsefulDataLength];
		for (int len = 0; len < UsefulDataLength; len++)
		{
			UsefulData[len] = ComPort.RX_BUF.get(7+len);
		}
		int Checksum = 0;
		for (int len = 3; len >= 0; len--)
		{
			Checksum = Checksum << 8;
			Checksum |= ComPort.RX_BUF.get(7+UsefulDataLength+len);
		}
		int End = 0;
		for (int len = 1; len >= 0; len--)
		{
			End = End << 8;
			End |= ComPort.RX_BUF.get(11+UsefulDataLength+len);
		}
		//Проверка контрольной суммы
		int RealChecksum = 0;
		byte buf[] = new byte[5 + UsefulDataLength];//буффер для расчета контрольной суммы
		buf[0] = (byte) AddressSender;
		buf[1] = (byte) (DataLength & 0xFF);
		buf[2] = (byte) (DataLength >> 8);
		buf[3] = (byte) ServiceByte;
		buf[4] = (byte) Cmd;
		for (int k = 0; k < UsefulDataLength; k++)
		{
			buf[k+5] = (byte) UsefulData[k];
		}
		RealChecksum = (int) FindCrc(buf, 5 + UsefulDataLength);
		if (RealChecksum != Checksum)
		{
			System.out.println("CRC error!");
		}
		//Отправка ответа в зависимости от команды
		switch (Cmd)
		{
			case 0x02:
				//если в отправляемой команде был указан адрес регистра с кодом ацп(1662)
				if ((TX_BUF[7]==0x7E)&&(TX_BUF[8]==0x06))
				{
					GUI.ADCCodeCh0.setText(	Integer.toString(UsefulData[0] | (UsefulData[1]<<8)));
					GUI.ADCCodeCh1.setText(	Integer.toString(UsefulData[2] | (UsefulData[3]<<8)));
					GUI.ADCCodeCh2.setText(	Integer.toString(UsefulData[4] | (UsefulData[5]<<8)));
					GUI.ADCCodeCh3.setText(	Integer.toString(UsefulData[6] | (UsefulData[7]<<8)));
					GUI.ADCCodeCh4.setText(	Integer.toString(UsefulData[8] | (UsefulData[9]<<8)));
					GUI.ADCCodeCh5.setText(	Integer.toString(UsefulData[10] | (UsefulData[11]<<8)));
					GUI.ADCCodeCh6.setText(	Integer.toString(UsefulData[12] | (UsefulData[13]<<8)));
					GUI.ADCCodeCh7.setText(	Integer.toString(UsefulData[14] | (UsefulData[15]<<8)));
					//пока сделаем так
					GUI.ADCValueCh0.setText(	Float.toString((UsefulData[0] | (UsefulData[1]<<8))*0.000814f+0.00438f));
					GUI.ADCValueCh1.setText(	Float.toString((UsefulData[2] | (UsefulData[3]<<8))*0.000814f+0.00438f));
					GUI.ADCValueCh2.setText(	Float.toString((UsefulData[4] | (UsefulData[5]<<8))*0.000814f+0.00438f));
					GUI.ADCValueCh3.setText(	Float.toString((UsefulData[6] | (UsefulData[7]<<8))*0.000814f+0.00438f));
					GUI.ADCValueCh4.setText(	Float.toString((UsefulData[8] | (UsefulData[9]<<8))*0.000814f+0.00438f));
					GUI.ADCValueCh5.setText(	Float.toString((UsefulData[10] | (UsefulData[11]<<8))*0.000814f+0.00438f));
					GUI.ADCValueCh6.setText(	Float.toString((UsefulData[12] | (UsefulData[13]<<8))*0.000814f+0.00438f));
					GUI.ADCValueCh7.setText(	Float.toString((UsefulData[14] | (UsefulData[15]<<8))*0.000814f+0.00438f));
				}
				else if (((TX_BUF[7]&0xFF) == 0xAE)&&((TX_BUF[8]&0xFF) ==0x06))
				{
					//если в отправляемой команде был указан адрес регистра с кодом ацп(1710)
					GUI.ADCValueCh0.setText(	Integer.toString(UsefulData[0] | (UsefulData[1]<<8)));
					GUI.ADCValueCh1.setText(	Integer.toString(UsefulData[2] | (UsefulData[3]<<8)));
					GUI.ADCValueCh2.setText(	Integer.toString(UsefulData[4] | (UsefulData[5]<<8)));
					GUI.ADCValueCh3.setText(	Integer.toString(UsefulData[6] | (UsefulData[7]<<8)));
					GUI.ADCValueCh4.setText(	Integer.toString(UsefulData[8] | (UsefulData[9]<<8)));
					GUI.ADCValueCh5.setText(	Integer.toString(UsefulData[10] | (UsefulData[11]<<8)));
					GUI.ADCValueCh6.setText(	Integer.toString(UsefulData[12] | (UsefulData[13]<<8)));
					GUI.ADCValueCh7.setText(	Integer.toString(UsefulData[14] | (UsefulData[15]<<8)));
				}
				//System.out.println(Arrays.toString(UsefulData));
				break;
		}
	}
	/*
	buf[] - масив с данными
	len - длина в байтах
	например для строки "123456789" - CRC = cbf43926
	*/
	public static long FindCrc(byte buf[], long len)
	{
		long crc_table[] = new long[256];
		long crc; int i, j;

		for (i = 0; i < 256; i++)
		{
			crc = i;
			for (j = 0; j < 8; j++)
				crc = ((crc & 1)==1) ? (crc >> 1) ^ 0xEDB88320L : crc >> 1;

			crc_table[i] = crc;
		};

		crc = 0xFFFFFFFFL;

		for(int k = 0; k < len; k++)
		{
			crc = crc_table[(int)((crc ^ buf[k]) & 0xFFL)] ^ (crc >> 8);
		}

		return crc ^ 0xFFFFFFFFL;
	}
}
