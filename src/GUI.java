import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import javax.swing.text.DefaultCaret;

public class GUI 
{
	public static JFrame window;
	public static JLabel comport_label;
	public static JComboBox ports;
	public static JButton ButtonConnect;
	//public static JButton ButtonSendData;
	public static JButton ButtonDisconnect;
	public static JLabel Cmdlabel;
	public static JButton ButtonCmdType;
	public static JButton ButtonCmdInit;
	public static JButton ButtonCmdReadCode;
	public static JButton ButtonCmdReadValueInt;
	public static JButton ButtonCmdWrite;
	public static JButton ButtonCmdReset;
	public static JButton ButtonCmdConfig;
	public static JLabel baudrate_label;
	public static JRadioButton RadioButton_9600;
	public static JRadioButton RadioButton_115200;
	public static JRadioButton RadioButton_256000;
	public static JRadioButton RadioButton_921600;
	public static ButtonGroup BaudRateGroup;
	public static JLabel status_window_label;
	public static JLabel RXData_label;
	public static JLabel TXData_label;
	public static JLabel UM_label;
	public static JLabel PM_label;
	public static JTextArea StatusTextArea;
	public static JTextArea RXTextArea;
	public static JTextArea TXTextArea;
	//public static JTextField DataTextField;
	public static JComboBox ReceiverModAddrJComboBox;
	public static JComboBox ReceiverChassisAddrJComboBox;
	public static JComboBox SenderModAddrJComboBox;
	public static JComboBox SenderChassisAddrJComboBox;
	public static JLabel UmModAddr;
	public static JLabel UmChassisAddr;
	public static JLabel PmModAddr;
	public static JLabel PmChassisAddr;
	public static JTextArea ADCCodeCh0;
	public static JTextArea ADCCodeCh1;
	public static JTextArea ADCCodeCh2;
	public static JTextArea ADCCodeCh3;
	public static JTextArea ADCCodeCh4;
	public static JTextArea ADCCodeCh5;
	public static JTextArea ADCCodeCh6;
	public static JTextArea ADCCodeCh7;
	public static JTextArea ADCValueCh0;
	public static JTextArea ADCValueCh1;
	public static JTextArea ADCValueCh2;
	public static JTextArea ADCValueCh3;
	public static JTextArea ADCValueCh4;
	public static JTextArea ADCValueCh5;
	public static JTextArea ADCValueCh6;
	public static JTextArea ADCValueCh7;
	
	public static void CreateWindow()
	{
		window = new JFrame("My TERMINAL");
		window.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		window.addWindowListener(new WindowAdapter() {
		    @Override
		    public void windowClosing(WindowEvent e) {
		        int confirm = JOptionPane.showOptionDialog(window,
		                    "Close?", "Exit", JOptionPane.YES_NO_OPTION,
		                    JOptionPane.QUESTION_MESSAGE, null, null, null);
		        if (confirm == JOptionPane.YES_OPTION) {
		           System.exit(1);
		           window.dispose();
		        }
		    }
		});
		
		comport_label = new JLabel("COM порт");
		comport_label.setBounds(190,0,90,20);
		window.add(comport_label);

		ports = new JComboBox(ComPort.portNames);
		ports.setBounds(180,20,90,20);
		ActionListener actionListener = new ActionListener() {
		    public void actionPerformed(ActionEvent e) 
		    {
		    	JComboBox box = (JComboBox)e.getSource();
		    	ComPort.CurrentPortName = (String)box.getSelectedItem();
		    }
		};
		ports.addActionListener(actionListener);
		window.add(ports);
		
		ActionListener ButtonConnectListener = new ActionListener() {
		    public void actionPerformed(ActionEvent e) 
		    {
		    	ComPort.Connect();
		    }
		};
		ButtonConnect = new JButton("Connect");
		ButtonConnect.addActionListener(ButtonConnectListener);
		ButtonConnect.setBounds(30,20,100,20);
		window.add(ButtonConnect);
			
		ActionListener ButtonDisconnectListener = new ActionListener() {
		    public void actionPerformed(ActionEvent e) 
		    {
		    	ComPort.Disconnect();
		    }
		};
		ButtonDisconnect = new JButton("Disconnect");
		ButtonDisconnect.setBounds(30,50,100,20);
		ButtonDisconnect.addActionListener(ButtonDisconnectListener);
		window.add(ButtonDisconnect);
		
		baudrate_label = new JLabel("Baud rate");
		baudrate_label.setBounds(190,40,90,20);
		window.add(baudrate_label);
			
		ActionListener RadioButtonListener = new ActionListener() 
		{
			public void actionPerformed(ActionEvent e)
			{
				if(e.getActionCommand().equals("9600"))
				{    
					ComPort.CurrentBaudRate = 9600;
		        }
				else if(e.getActionCommand().equals("115200"))
				{    
					ComPort.CurrentBaudRate = 115200;
		        }
				else if(e.getActionCommand().equals("256000"))
				{    
					ComPort.CurrentBaudRate = 256000;
		        }
				else if(e.getActionCommand().equals("921600"))
				{    
					ComPort.CurrentBaudRate = 921600;
		        }
			}
		};
		RadioButton_9600 = new JRadioButton();
		RadioButton_9600.setBounds(160,55,70,20);
		RadioButton_9600.setText("9600");
		RadioButton_9600.addActionListener(RadioButtonListener);
		window.add(RadioButton_9600);
		
		RadioButton_115200 = new JRadioButton();
		RadioButton_115200.setBounds(160,75,70,20);
		RadioButton_115200.setText("115200");
		RadioButton_115200.addActionListener(RadioButtonListener);
		RadioButton_115200.setSelected(true);
		window.add(RadioButton_115200);
		
		RadioButton_256000 = new JRadioButton();
		RadioButton_256000.setBounds(230,55,70,20);
		RadioButton_256000.setText("256000");
		RadioButton_256000.addActionListener(RadioButtonListener);
		window.add(RadioButton_256000);
		
		RadioButton_921600 = new JRadioButton();
		RadioButton_921600.setBounds(230,75,70,20);
		RadioButton_921600.setText("921600");
		RadioButton_921600.addActionListener(RadioButtonListener);
		window.add(RadioButton_921600);
		
		BaudRateGroup = new ButtonGroup();
		BaudRateGroup.add(RadioButton_9600);
		BaudRateGroup.add(RadioButton_115200);
		BaudRateGroup.add(RadioButton_256000);
		BaudRateGroup.add(RadioButton_921600);
		
		
		status_window_label = new JLabel("STATUS WINDOW");
		status_window_label.setBounds(30,80,150,20);
		window.add(status_window_label);
		
		StatusTextArea = new JTextArea(15, 10);
		StatusTextArea.setLineWrap(true);
		StatusTextArea.setWrapStyleWord(true);
		StatusTextArea.setBounds(10, 100, 290, 35);
		window.add(StatusTextArea);
		
		TXData_label = new JLabel("TRANSMITTED DATA");
		TXData_label.setBounds(30,132,150,20);
		window.add(TXData_label);
		
		TXTextArea = new JTextArea(15, 10);
		TXTextArea.setLineWrap(true);
		TXTextArea.setWrapStyleWord(true);
		TXTextArea.setBounds(10, 150, 290, 35);
		window.add(TXTextArea);
		
		RXData_label = new JLabel("RECEIVED DATA");
		RXData_label.setBounds(30,182,150,20);
		window.add(RXData_label);
		
		RXTextArea = new JTextArea(15, 10);
		RXTextArea.setLineWrap(true);
		RXTextArea.setWrapStyleWord(true);
		RXTextArea.setBounds(10, 200, 290, 35);
		window.add(RXTextArea);
		
		/*ActionListener ButtonSendDataListener = new ActionListener() {
		    public void actionPerformed(ActionEvent e) 
		    {
		    	//ComPort.SendData(MODBUS.DATA + "\r");
		    }
		};
		ButtonSendData = new JButton("Send DATA");
		ButtonSendData.addActionListener(ButtonSendDataListener);
		ButtonSendData.setBounds(320,80,100,20);
		window.add(ButtonSendData);*/
		
		Cmdlabel = new JLabel("Поддерживаемые команды");
		Cmdlabel.setBounds(340,115,190,20);
		window.add(Cmdlabel);
		
		ActionListener ButtonTYPEListener = new ActionListener() {
		    public void actionPerformed(ActionEvent e) 
		    {
		    	//очищаем буфер приема
		    	ComPort.RX_BUF.clear();
		    	List<Byte> data = Arrays.asList((byte)0x00);
		    	MODBUS.ModbusSendData((byte)MODBUS.PmModAddr, (byte)MODBUS.PmChassisAddr, (byte)MODBUS.UmModAddr, (byte)MODBUS.UmChassisAddr, (byte)0x00, data, (byte)1, (byte)1);
		    }
		};
		ButtonCmdType = new JButton("TYPE");
		ButtonCmdType.setBounds(320,140,90,20);
		ButtonCmdType.addActionListener(ButtonTYPEListener);
		window.add(ButtonCmdType);
		
		ActionListener ButtonINITListener = new ActionListener() {
		    public void actionPerformed(ActionEvent e) 
		    {
		    	//очищаем буфер приема
		    	ComPort.RX_BUF.clear();
		    	List<Byte> data = Arrays.asList((byte)0x01);
		    	MODBUS.ModbusSendData((byte)MODBUS.PmModAddr, (byte)MODBUS.PmChassisAddr, (byte)MODBUS.UmModAddr, (byte)MODBUS.UmChassisAddr, (byte)0x01, data, (byte)1, (byte)1);		
		    }
		};
		ButtonCmdInit = new JButton("INIT");
		ButtonCmdInit.setBounds(320,180,90,20);
		ButtonCmdInit.addActionListener(ButtonINITListener);
		window.add(ButtonCmdInit);
		
		ActionListener ButtonREADCodeListener = new ActionListener() {
		    public void actionPerformed(ActionEvent e) 
		    {
		    	//очищаем буфер приема
		    	ComPort.RX_BUF.clear();
		    	//отправляем адрес 1662 и размер 16 байт
		    	List<Byte> data = Arrays.asList((byte)0x7E,(byte)0x06,(byte)0x10,(byte)0x00);
		    	MODBUS.ModbusSendData((byte)MODBUS.PmModAddr, (byte)MODBUS.PmChassisAddr, (byte)MODBUS.UmModAddr, (byte)MODBUS.UmChassisAddr, (byte)0x02, data, (byte)4, (byte)1);	
		    }
		};
		ButtonCmdReadCode = new JButton("READ ADC CODE");
		ButtonCmdReadCode.setBounds(425,140,160,20);
		ButtonCmdReadCode.addActionListener(ButtonREADCodeListener);
		window.add(ButtonCmdReadCode);
		
		ActionListener ButtonREADValueIntListener = new ActionListener() {
		    public void actionPerformed(ActionEvent e) 
		    {
		    	//очищаем буфер приема
		    	ComPort.RX_BUF.clear();
		    	//отправляем адрес 1710 и размер 16 байт
		    	List<Byte> data = Arrays.asList((byte)0xAE,(byte)0x06,(byte)0x10,(byte)0x00);
		    	MODBUS.ModbusSendData((byte)MODBUS.PmModAddr, (byte)MODBUS.PmChassisAddr, (byte)MODBUS.UmModAddr, (byte)MODBUS.UmChassisAddr, (byte)0x02, data, (byte)4, (byte)1);	
		    }
		};
		ButtonCmdReadValueInt = new JButton("READ ADC VALUE");
		ButtonCmdReadValueInt.setBounds(425,180,160,20);
		ButtonCmdReadValueInt.addActionListener(ButtonREADValueIntListener);
		window.add(ButtonCmdReadValueInt);
		
		ActionListener ButtonWRITEListener = new ActionListener() {
		    public void actionPerformed(ActionEvent e) 
		    {
		    	//очищаем буфер приема
		    	ComPort.RX_BUF.clear();
		    }
		};
		ButtonCmdWrite = new JButton("WRITE");
		ButtonCmdWrite.setBounds(600,140,90,20);
		ButtonCmdWrite.addActionListener(ButtonWRITEListener);
		window.add(ButtonCmdWrite);
		
		ActionListener ButtonRESETListener = new ActionListener() {
		    public void actionPerformed(ActionEvent e) 
		    {
		    	//очищаем буфер приема
		    	ComPort.RX_BUF.clear();
		    	List<Byte> data = Arrays.asList((byte)0x04);
		    	MODBUS.ModbusSendData((byte)MODBUS.PmModAddr, (byte)MODBUS.PmChassisAddr, (byte)MODBUS.UmModAddr, (byte)MODBUS.UmChassisAddr, (byte)0x04, data, (byte)1, (byte)1);				 
		    }
		};
		ButtonCmdReset = new JButton("RESET");
		ButtonCmdReset.setBounds(320,220,90,20);
		ButtonCmdReset.addActionListener(ButtonRESETListener);
		window.add(ButtonCmdReset);
		
		ActionListener ButtonCONFIGListener = new ActionListener() {
		    public void actionPerformed(ActionEvent e) 
		    {
		    	//очищаем буфер приема
		    	ComPort.RX_BUF.clear();
		    }
		};
		ButtonCmdConfig = new JButton("CONFIG");
		ButtonCmdConfig.setBounds(600,180,90,20);
		ButtonCmdConfig.addActionListener(ButtonCONFIGListener);
		window.add(ButtonCmdConfig);
		
		/*DataTextField = new JTextField();
		DataTextField.setBounds(320,110,170,20);
		//DataTextField.addActionListener(DataTextFieldListener);
		DataTextField.addActionListener(new ActionListener() {
		      public void actionPerformed(ActionEvent e) {
		    	  MODBUS.DATA =  DataTextField.getText();
		      }
		    });
		window.add(DataTextField);*/
		
		UM_label = new JLabel("Адрес УМ");
		UM_label.setBounds(340,10,90,20);
		window.add(UM_label);
		
		PM_label = new JLabel("Адрес ПМ");
		PM_label.setBounds(440,10,90,20);
		window.add(PM_label);
		
		String address[] = {"0","1","2","3","4","5","6","7","8","9","10","11","12","13","14","15"};
		ActionListener ReceiverModAddrJComboBoxListener = new ActionListener() {
		    public void actionPerformed(ActionEvent e) 
		    {
		    	JComboBox box = (JComboBox)e.getSource();
		    	MODBUS.PmModAddr = Integer.parseInt((String) box.getSelectedItem());
		    }
		};
		ReceiverModAddrJComboBox = new JComboBox(address);
		ReceiverModAddrJComboBox.setBounds(430,50,90,20);
		ReceiverModAddrJComboBox.addActionListener(ReceiverModAddrJComboBoxListener);
		window.add(ReceiverModAddrJComboBox);
		
		ActionListener ReceiverChassisAddrJComboBoxListener = new ActionListener() {
		    public void actionPerformed(ActionEvent e) 
		    {
		    	JComboBox box = (JComboBox)e.getSource();
		    	MODBUS.PmChassisAddr = Integer.parseInt((String) box.getSelectedItem());
		    }
		};
		ReceiverChassisAddrJComboBox = new JComboBox(address);
		ReceiverChassisAddrJComboBox.setBounds(430,90,90,20);
		ReceiverChassisAddrJComboBox.addActionListener(ReceiverChassisAddrJComboBoxListener);
		window.add(ReceiverChassisAddrJComboBox);
		
		ActionListener SenderModAddrJComboBoxListener = new ActionListener() {
		    public void actionPerformed(ActionEvent e) 
		    {
		    	JComboBox box = (JComboBox)e.getSource();
		    	MODBUS.UmModAddr = Integer.parseInt((String) box.getSelectedItem());
		    }
		};
		SenderModAddrJComboBox = new JComboBox(address);
		SenderModAddrJComboBox.setBounds(320,50,90,20);
		SenderModAddrJComboBox.addActionListener(SenderModAddrJComboBoxListener);
		window.add(SenderModAddrJComboBox);
		
		ActionListener SenderChassisAddrJComboBoxListener = new ActionListener() {
		    public void actionPerformed(ActionEvent e) 
		    {
		    	JComboBox box = (JComboBox)e.getSource();
		    	MODBUS.UmChassisAddr = Integer.parseInt((String) box.getSelectedItem());
		    }
		};
		SenderChassisAddrJComboBox = new JComboBox(address);
		SenderChassisAddrJComboBox.setBounds(320,90,90,20);
		SenderChassisAddrJComboBox.addActionListener(SenderChassisAddrJComboBoxListener);
		window.add(SenderChassisAddrJComboBox);
		
		UmModAddr = new JLabel("Адрес модуля");
		UmModAddr.setBounds(430,30,90,20);
		window.add(UmModAddr);

		UmChassisAddr = new JLabel("Адрес шасси");
		UmChassisAddr.setBounds(430,70,90,20);
		window.add(UmChassisAddr);
		
		PmModAddr = new JLabel("Адрес модуля");
		PmModAddr.setBounds(320,30,90,20);
		window.add(PmModAddr);
		
		PmChassisAddr = new JLabel("Адрес шасси");
		PmChassisAddr.setBounds(320,70,90,20);
		window.add(PmChassisAddr);
			
		JLabel ADCResultsLabel = new JLabel("Результаты измерений АЦП по всем каналам");
		ADCResultsLabel.setBounds(140,245,290,20);
		window.add(ADCResultsLabel);
		JLabel ADCChanelsLabel = new JLabel("Канал 0 \t   Канал 1 \t   Канал 2 \t   Канал 3 \t   Канал 4 \t   Канал 5 \t   Канал 6 \t   Канал 7 ");
		ADCChanelsLabel.setBounds(60,265,490,20);
		window.add(ADCChanelsLabel);
		JLabel ADCCodeLabel = new JLabel("Код");
		ADCCodeLabel.setBounds(20,295,90,20);
		window.add(ADCCodeLabel);
		JLabel ADCValueLabel = new JLabel("U, В");
		ADCValueLabel.setBounds(20,325,90,20);
		window.add(ADCValueLabel);
		
		ADCCodeCh0 = new JTextArea();
		ADCCodeCh0.setLineWrap(true);
		ADCCodeCh0.setWrapStyleWord(true);
		ADCCodeCh0.setBounds(60, 295, 45, 20);
		window.add(ADCCodeCh0);
		
		ADCCodeCh1 = new JTextArea();
		ADCCodeCh1.setLineWrap(true);
		ADCCodeCh1.setWrapStyleWord(true);
		ADCCodeCh1.setBounds(118, 295, 45, 20);
		window.add(ADCCodeCh1);
		
		ADCCodeCh2 = new JTextArea();
		ADCCodeCh2.setLineWrap(true);
		ADCCodeCh2.setWrapStyleWord(true);
		ADCCodeCh2.setBounds(176, 295, 45, 20);
		window.add(ADCCodeCh2);
		
		ADCCodeCh3 = new JTextArea();
		ADCCodeCh3.setLineWrap(true);
		ADCCodeCh3.setWrapStyleWord(true);
		ADCCodeCh3.setBounds(234, 295, 45, 20);
		window.add(ADCCodeCh3);
		
		ADCCodeCh4 = new JTextArea();
		ADCCodeCh4.setLineWrap(true);
		ADCCodeCh4.setWrapStyleWord(true);
		ADCCodeCh4.setBounds(292, 295, 45, 20);
		window.add(ADCCodeCh4);
		
		ADCCodeCh5 = new JTextArea();
		ADCCodeCh5.setLineWrap(true);
		ADCCodeCh5.setWrapStyleWord(true);
		ADCCodeCh5.setBounds(350, 295, 45, 20);
		window.add(ADCCodeCh5);
		
		ADCCodeCh6 = new JTextArea();
		ADCCodeCh6.setLineWrap(true);
		ADCCodeCh6.setWrapStyleWord(true);
		ADCCodeCh6.setBounds(408, 295, 45, 20);
		window.add(ADCCodeCh6);
		
		ADCCodeCh7 = new JTextArea();
		ADCCodeCh7.setLineWrap(true);
		ADCCodeCh7.setWrapStyleWord(true);
		ADCCodeCh7.setBounds(466, 295, 45, 20);
		window.add(ADCCodeCh7);
		
		ADCValueCh0 = new JTextArea();
		ADCValueCh0.setLineWrap(true);
		ADCValueCh0.setWrapStyleWord(true);
		ADCValueCh0.setBounds(60, 325, 45, 20);
		window.add(ADCValueCh0);
		
		ADCValueCh1 = new JTextArea();
		ADCValueCh1.setLineWrap(true);
		ADCValueCh1.setWrapStyleWord(true);
		ADCValueCh1.setBounds(118, 325, 45, 20);
		window.add(ADCValueCh1);
		
		ADCValueCh2 = new JTextArea();
		ADCValueCh2.setLineWrap(true);
		ADCValueCh2.setWrapStyleWord(true);
		ADCValueCh2.setBounds(176, 325, 45, 20);
		window.add(ADCValueCh2);
		
		ADCValueCh3 = new JTextArea();
		ADCValueCh3.setLineWrap(true);
		ADCValueCh3.setWrapStyleWord(true);
		ADCValueCh3.setBounds(234, 325, 45, 20);
		window.add(ADCValueCh3);
		
		ADCValueCh4 = new JTextArea();
		ADCValueCh4.setLineWrap(true);
		ADCValueCh4.setWrapStyleWord(true);
		ADCValueCh4.setBounds(292, 325, 45, 20);
		window.add(ADCValueCh4);
		
		ADCValueCh5 = new JTextArea();
		ADCValueCh5.setLineWrap(true);
		ADCValueCh5.setWrapStyleWord(true);
		ADCValueCh5.setBounds(350, 325, 45, 20);
		window.add(ADCValueCh5);
		
		ADCValueCh6 = new JTextArea();
		ADCValueCh6.setLineWrap(true);
		ADCValueCh6.setWrapStyleWord(true);
		ADCValueCh6.setBounds(408, 325, 45, 20);
		window.add(ADCValueCh6);
		
		ADCValueCh7 = new JTextArea();
		ADCValueCh7.setLineWrap(true);
		ADCValueCh7.setWrapStyleWord(true);
		ADCValueCh7.setBounds(466, 325, 45, 20);
		window.add(ADCValueCh7);
		
		window.setSize(720,400);
		window.setLayout(null);
		window.setVisible(true);
	}
}
