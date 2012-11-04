package com.yuchs.yuchcaller;

import local.yuchcallerlocalResource;
import net.rim.blackberry.api.phone.Phone;
import net.rim.blackberry.api.phone.PhoneCall;
import net.rim.blackberry.api.phone.phonegui.PhoneScreen;
import net.rim.blackberry.api.phone.phonegui.ScreenModel;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.component.LabelField;

public class CallScreenPlugin {
	
	// buffer the former phone number avoid search phone data many times
	private String m_formerNumber = "";
	private String m_formerLocation = "";
	
	public static final int	INCOMING =  1;
	public static final int	WAITING =  1;
	public static final int	OUTGOING =  2;
	public static final int	ACTIVECALL =  2;
	
	final private YuchCaller	m_mainApp;
	
	public CallScreenPlugin(YuchCaller _mainApp){
		m_mainApp = _mainApp;
	}

	// applay the 
	public void apply(final int callId,final int _screenType){
		
		try{

			ScreenModel screenModel = new ScreenModel(callId);
			if(!screenModel.isSupported()){
				m_mainApp.SetErrorString("screenModel.isSupported() is false , device is locked!");
				return ;
			}
			
			PhoneCall t_pc				= Phone.getCall(callId);
			String t_number 			= YuchCaller.parsePhoneNumber(t_pc.getPhoneNumber());
			
			m_formerLocation			= m_formerNumber.equals(t_number)?m_formerLocation:m_mainApp.searchLocation(t_number);
			m_formerNumber				= t_number;
						
			PhoneScreen ps = screenModel.getPhoneScreen(ScreenModel.getCurrentOrientation(), _screenType);
			
			final Font t_textFont = m_mainApp.generateLocationTextFont();
			final int t_width		= t_textFont.getAdvance(m_formerLocation);
			final int t_height	= t_textFont.getHeight();
			
			LabelField t_label = new LabelField(m_formerLocation,Field.NON_FOCUSABLE){
				public void paint(Graphics g){
					int t_color = g.getColor();
					try{
						g.setColor(YuchCallerProp.instance().getLocationColor());
						g.setFont(t_textFont);
						g.drawText(m_formerLocation, 0, 0);
					}finally{
						g.setColor(t_color);
					}
				}
				
				public int getPreferredWidth(){
					return t_width;
				}
				
				public int getPreferredHeight(){
					return t_height;
				}
				
				protected void layout(int _width,int _height){
					setExtent(getPreferredWidth(), getPreferredHeight());
				}
			};
			
			ps.add(t_label);	
			screenModel.sendAllDataToScreen();			
			
		}catch(Exception e){
			m_mainApp.SetErrorString("CSPA",e);
		}
	}
	
	/**
	 * clear the buffered number 
	 */
	public void clearBufferedNumber(){
		m_formerNumber 	= "";
		m_formerLocation= "";
	}
}

