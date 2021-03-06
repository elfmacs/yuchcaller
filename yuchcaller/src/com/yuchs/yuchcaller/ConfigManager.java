/**
 *  Dear developer:
 *  
 *   If you want to modify this file of project and re-publish this please visit:
 *  
 *     http://code.google.com/p/yuchberry/wiki/Project_files_header
 *     
 *   to check your responsibility and my humble proposal. Thanks!
 *   
 *  -- 
 *  Yuchs' Developer    
 *  
 *  
 *  
 *  
 *  尊敬的开发者：
 *   
 *    如果你想要修改这个项目中的文件，同时重新发布项目程序，请访问一下：
 *    
 *      http://code.google.com/p/yuchberry/wiki/Project_files_header
 *      
 *    了解你的责任，还有我卑微的建议。 谢谢！
 *   
 *  -- 
 *  语盒开发者
 *  
 */
package com.yuchs.yuchcaller;

import java.util.Vector;

import local.yuchcallerlocalResource;
import net.rim.device.api.system.Characters;
import net.rim.device.api.system.Clipboard;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.component.ActiveRichTextField;
import net.rim.device.api.ui.component.AutoTextEditField;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.component.CheckboxField;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.EditField;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.NullField;
import net.rim.device.api.ui.component.ObjectChoiceField;
import net.rim.device.api.ui.component.SeparatorField;
import net.rim.device.api.ui.component.TextField;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.VerticalFieldManager;
import net.rim.device.api.util.MathUtilities;

import com.yuchs.yuchcaller.sync.SyncOptionManager;

public class ConfigManager extends VerticalFieldManager implements FieldChangeListener {
	
	//! location candidate color
	private static final int[]		fsm_locationCandColor = 
	{
		0,
		0xffffff,
		
		0xf7d8a2,
		0x52aa80,
		0x0b93df,
		0xcb4ae2,
		
		0xfef171,
		0xfdbe55,
		0x8a8dfe,
		0xd20005,
		
		0x59d200,
		0x44c778,
		0xbc9543,
		0xaba8a8,
	};
	
	private static final int[]		fsm_locationCandColorStr = 
	{
		0,
		0,
		
		yuchcallerlocalResource.PHONE_CONFIG_COLOR_1,
		yuchcallerlocalResource.PHONE_CONFIG_COLOR_2,
		yuchcallerlocalResource.PHONE_CONFIG_COLOR_3,
		yuchcallerlocalResource.PHONE_CONFIG_COLOR_4,
		
		yuchcallerlocalResource.PHONE_CONFIG_COLOR_5,
		yuchcallerlocalResource.PHONE_CONFIG_COLOR_6,
		yuchcallerlocalResource.PHONE_CONFIG_COLOR_7,
		yuchcallerlocalResource.PHONE_CONFIG_COLOR_8,
		
		yuchcallerlocalResource.PHONE_CONFIG_COLOR_9,
		yuchcallerlocalResource.PHONE_CONFIG_COLOR_10,
		yuchcallerlocalResource.PHONE_CONFIG_COLOR_11,
		yuchcallerlocalResource.PHONE_CONFIG_COLOR_12,		
	};
			
	//! font to sub label 
	private Font		m_subLabelBoldFont = getFont().derive(getFont().getStyle() | Font.BOLD);
	
	//! font of clickable label 
	private Font		m_mainLabelBoldFont = getFont().derive(getFont().getStyle() | Font.BOLD ,getFont().getHeight() * 6 / 5);
	
	//! the config editField of recv-phone vibration
	private EditField m_recvVibrationTime = null;
	
	//! the config editField of hangup-phone vibration
	private EditField m_hangupVibrationTime = null;
	private EditField m_locationInfoPosition_x = null;	
	private EditField m_locationInfoPosition_y = null;
	
	private ObjectChoiceField m_locationInfoColor = null;	
	private EditField m_locationInfoHeight			= null;
	private CheckboxField	m_locationBoldFont		= null;
	private EditField m_searchNumberInput			= null;
	
	private EditField 		m_IPDialPrefix			= null;
	private CheckboxField	mShowPhoneCallTime		= null;
	private CheckboxField	m_showSystemMenu		= null;
	private CheckboxField	mDisableLocationInfo	= null;
	
	private ColorSampleField m_locationTextColorSample = null;
	
	private ButtonField		m_showDebugScreenBtn	= null;
	private ButtonField		mCheckVersionBtn		= null;
	
	//! advance switch label
	private LabelField	m_advanceSwitchLabel		= null;
	
	//! parent manager for advance attribute edit field
	private VerticalFieldManager	m_advanceManager	= null;
	private NullField				m_advanceManagerNull= new NullField(Field.NON_FOCUSABLE);
	
	
	//! the search result list
	private SearchResultListMgr		m_intelSearchListMgr = new SearchResultListMgr();
	
	//! intel search input edit field
	private EditField				m_intelSearchInput = null;
	
	//! matched special number
	private Vector					m_matchedList	= new Vector();
	
	//! a page number to display matchedList
	private final	int 			MatchedLabelPageNum	= 5;
	
	//! currrent matched special number index
	private int					m_addMatchedSNIdx		= 0;
	private ClickLabel				m_matchedNextPageLabel	= null;
	
	//! matched special number display allocate poor
	private Vector					m_allocList		= new Vector();
	
	//! about switch label
	private ClickLabel				m_aboutSwitchLabel	= null;
	
	//! the RichText field to show about text
	private ActiveRichTextField		m_aboutTextField	= null;
	private NullField				m_aboutTextFieldNull = new NullField(Field.NON_FOCUSABLE);
	
	//! sync click bale
	private ClickLabel				mSyncSwitchLabel	= null;
	
	//! the sync field null
	private SyncOptionManager		mSyncField		= null;
	private NullField				mSyncFieldNull	= new NullField(Field.NON_FOCUSABLE);		
	
	//! sync error text field
	private ActiveRichTextField		mSyncErrorTextField = null;
	private ActiveRichTextField		mSyncInfoTextField = null;
	
	private YuchCaller	m_mainApp = null;
	
	public ConfigManager(YuchCaller _mainApp){
		
		m_mainApp = _mainApp;
		m_mainApp.m_configManager = this;
				
		// search the phone
		LabelField t_label = new LabelField(m_mainApp.m_local.getString(yuchcallerlocalResource.PHONE_CONFIG_SEARCH),Field.NON_FOCUSABLE);
		t_label.setFont(m_subLabelBoldFont);
		add(t_label);
		
		// create the EditField to return dirty false
		m_searchNumberInput		= new InputEditField(m_mainApp.m_local.getString(yuchcallerlocalResource.PHONE_CONFIG_SEARCH_PROMPT),
														EditField.FILTER_NUMERIC);
		
		add(m_searchNumberInput);
		m_searchNumberInput.setChangeListener(this);
		
		// initialize the sample
		m_locationTextColorSample = new ColorSampleField(m_mainApp.getProperties().getLocationColor());
		m_locationTextColorSample.setFont(m_mainApp.generateLocationTextFont());
		add(m_locationTextColorSample);
		
		// add SeparatorField
		add(new SeparatorField(Field.NON_FOCUSABLE));
		
		t_label	= new LabelField(_mainApp.m_local.getString(yuchcallerlocalResource.PHONE_CONFIG_INTEL_SEARCH),Field.NON_FOCUSABLE);
		t_label.setFont(m_subLabelBoldFont);
		add(t_label);
		
		m_intelSearchInput	= new InputEditField(m_mainApp.m_local.getString(yuchcallerlocalResource.PHONE_CONFIG_INTEL_SEARCH_PROMPT),0);
		add(m_intelSearchInput);
		m_intelSearchInput.setChangeListener(this);
		
		add(m_intelSearchListMgr);
		
		// add SeparatorField
		add(new SeparatorField(Field.NON_FOCUSABLE));
		
		// add the switch
		m_advanceSwitchLabel		= new ClickLabel(getAdvanceSwitchLabelText());		
		m_advanceSwitchLabel.setFont(m_mainLabelBoldFont);
		m_advanceSwitchLabel.setChangeListener(this);
		add(m_advanceSwitchLabel);
		add(m_advanceManagerNull);
		
		// add the about label
		m_aboutSwitchLabel			= new ClickLabel(getAboutSwitchLabelText());
		m_aboutSwitchLabel.setFont(m_mainLabelBoldFont);
		m_aboutSwitchLabel.setChangeListener(this);
		add(m_aboutSwitchLabel);
		add(m_aboutTextFieldNull);
		
		// add the sync label
		mSyncSwitchLabel			= new ClickLabel(getSyncConfigSwitchLabelText());
		mSyncSwitchLabel.setFont(m_mainLabelBoldFont);
		mSyncSwitchLabel.setChangeListener(this);
		add(mSyncSwitchLabel);
		add(mSyncFieldNull);
				
		// check the clipboard text is phone number or not 
		Object t_clipboard = Clipboard.getClipboard().get();
		if(t_clipboard != null){
			
			String t_clipText = t_clipboard.toString();
			
			if(DbIndex.isPhoneNumber(t_clipText)){
				m_searchNumberInput.setText(t_clipText);
				fieldChanged(m_searchNumberInput, 0);
			}		
		}
		
		// refresh the sync prompt
		refreshSyncPromptImpl();
	}
	
	private void showOrHideAdvanceSetting(){
		
		if(m_advanceManager == null){
			
			// initialize the ObjectChoiceField select colorField and find the index of current color
			int t_choiceIdx = 0;
			Object[] t_choiceObj = new Object[fsm_locationCandColor.length];
			for(int i = 0;i < fsm_locationCandColor.length;i++){
				t_choiceObj[i] = new ColorChoiceField(fsm_locationCandColor[i]);
				
				if(fsm_locationCandColor[i] == m_mainApp.getProperties().getLocationColor()){
					t_choiceIdx = i;				
				}
			}
			
			m_advanceManager = new VerticalFieldManager();
			
			LabelField t_label = new LabelField(m_mainApp.m_local.getString(yuchcallerlocalResource.PHONE_VIBRATION),Field.NON_FOCUSABLE);
			t_label.setFont(m_subLabelBoldFont);
			m_advanceManager.add(t_label);
			
			m_recvVibrationTime = new EditField(m_mainApp.m_local.getString(yuchcallerlocalResource.PHONE_RECV_VIBRATE_TIME),
												Integer.toString(m_mainApp.getProperties().getRecvPhoneVibrationTime()),
												// Vibration time in milliseconds, from 0 to 25500.
												//
												4,
												EditField.NO_NEWLINE | EditField.FILTER_NUMERIC );
			
			m_hangupVibrationTime = new EditField(m_mainApp.m_local.getString(yuchcallerlocalResource.PHONE_HANGUP_VIBRATE_TIME),
												Integer.toString(m_mainApp.getProperties().getHangupPhoneVibrationTime()),
												// Vibration time in milliseconds, from 0 to 25500.
												//
												4,
												EditField.NO_NEWLINE | EditField.FILTER_NUMERIC );
			m_advanceManager.add(m_recvVibrationTime);
			m_advanceManager.add(m_hangupVibrationTime);
			
			
			// separator
			m_advanceManager.add(new SeparatorField());
			
			t_label = new LabelField(m_mainApp.m_local.getString(yuchcallerlocalResource.PHONE_CALL),Field.NON_FOCUSABLE);
			t_label.setFont(m_subLabelBoldFont);
			m_advanceManager.add(t_label);
			
			String t_pos_prompt_x;
			String t_pos_prompt_y;
			
			if(m_mainApp.getOSVersion().startsWith("4.")){
				t_pos_prompt_x = m_mainApp.m_local.getString(yuchcallerlocalResource.PHONE_CALL_TEXT_POSITION_X);
				t_pos_prompt_y = m_mainApp.m_local.getString(yuchcallerlocalResource.PHONE_CALL_TEXT_POSITION_Y);
			}else{
				t_pos_prompt_x = m_mainApp.m_local.getString(yuchcallerlocalResource.PHONE_CALL_TEXT_POSITION_OFFSET_X);
				t_pos_prompt_y = m_mainApp.m_local.getString(yuchcallerlocalResource.PHONE_CALL_TEXT_POSITION_OFFSET_Y);
			}
			
			m_locationInfoPosition_x = new EditField(t_pos_prompt_x,
													Integer.toString(m_mainApp.getProperties().getLocationPosition_x()),
													4,
													EditField.NO_NEWLINE | EditField.FILTER_INTEGER );

			m_locationInfoPosition_y = new EditField(t_pos_prompt_y,
													Integer.toString(m_mainApp.getProperties().getLocationPosition_y()),
													4,
													EditField.NO_NEWLINE | EditField.FILTER_INTEGER );
			
			
			
			m_advanceManager.add(m_locationInfoPosition_x);
			m_advanceManager.add(m_locationInfoPosition_y);
			
			
			m_locationInfoHeight = new EditField(m_mainApp.m_local.getString(yuchcallerlocalResource.PHONE_CALL_TEXT_HEIGHT),
					Integer.toString(m_mainApp.getProperties().getLocationHeight()),
					2,
					EditField.NO_NEWLINE | EditField.FILTER_NUMERIC );

			m_locationInfoHeight.setChangeListener(this);
			m_advanceManager.add(m_locationInfoHeight);
						
			m_locationInfoColor	= new ObjectChoiceField(m_mainApp.m_local.getString(yuchcallerlocalResource.PHONE_CALL_TEXT_COLOR),
														t_choiceObj,t_choiceIdx);
			
			m_advanceManager.add(m_locationInfoColor);
			m_locationInfoColor.setChangeListener(this);
			
			m_locationBoldFont = new CheckboxField(m_mainApp.m_local.getString(yuchcallerlocalResource.PHONE_CALL_TEXT_BOLD_STYLE), 
													m_mainApp.getProperties().isBoldFont());
			
			m_locationBoldFont.setChangeListener(this);
			m_advanceManager.add(m_locationBoldFont);
			
			m_advanceManager.add(new SeparatorField());
			
			t_label = new LabelField(m_mainApp.m_local.getString(yuchcallerlocalResource.PHONE_CONFIG_OTHER),Field.NON_FOCUSABLE);
			t_label.setFont(m_subLabelBoldFont);
			m_advanceManager.add(t_label);
			
			m_IPDialPrefix = new EditField(m_mainApp.m_local.getString(yuchcallerlocalResource.PHONE_CONFIG_IP_DIAL),m_mainApp.getProperties().getIPDialNumber(),
											10,EditField.NO_NEWLINE | EditField.FILTER_NUMERIC);
			
			m_advanceManager.add(m_IPDialPrefix);
			
			mShowPhoneCallTime = new CheckboxField(m_mainApp.m_local.getString(yuchcallerlocalResource.PHONE_CONFIG_SHOW_PHONECALL_LENGTH), 
													m_mainApp.getProperties().isShowPhoneCallTimeLen());
			m_advanceManager.add(mShowPhoneCallTime);
			
			m_showSystemMenu	= new CheckboxField(m_mainApp.m_local.getString(yuchcallerlocalResource.PHONE_CONFIG_SHOW_SYSTEM_MENU), m_mainApp.getProperties().showSystemMenu());
			m_advanceManager.add(m_showSystemMenu);
			
			mDisableLocationInfo	= new CheckboxField(m_mainApp.m_local.getString(yuchcallerlocalResource.PHONE_CONFIG_ENABLE_CALLER), !m_mainApp.getProperties().isEnableCaller());
			m_advanceManager.add(mDisableLocationInfo);			
						
			// add the check version button and show debug Screen button
			//
			HorizontalFieldManager t_btnMgr = new HorizontalFieldManager(Manager.NO_HORIZONTAL_SCROLL);
			mCheckVersionBtn		= new ButtonField(m_mainApp.m_local.getString(yuchcallerlocalResource.PHONE_CONFIG_CHECK_VERSION_BTN),
															ButtonField.CONSUME_CLICK | ButtonField.NEVER_DIRTY);
			t_btnMgr.add(mCheckVersionBtn);
			mCheckVersionBtn.setChangeListener(this);

			m_showDebugScreenBtn	= new ButtonField(m_mainApp.m_local.getString(yuchcallerlocalResource.PHONE_CONFIG_SHOW_DEBUG),
														ButtonField.CONSUME_CLICK | ButtonField.NEVER_DIRTY);
			t_btnMgr.add(m_showDebugScreenBtn);
			m_showDebugScreenBtn.setChangeListener(this);
			
			m_advanceManager.add(t_btnMgr);
		}
		
		if(m_advanceManagerNull.getManager() != null){
			replace(m_advanceManagerNull, m_advanceManager);
		}else{
			replace(m_advanceManager, m_advanceManagerNull);
		}
		
		m_advanceSwitchLabel.setText(getAdvanceSwitchLabelText());
	}
	
	private String getAdvanceSwitchLabelText(){
		return (m_advanceManagerNull.getManager() != null || m_advanceManager == null)?
						("(+)" + m_mainApp.m_local.getString(yuchcallerlocalResource.PHONE_CONFIG_ADVANCE)):
						"(-)" + m_mainApp.m_local.getString(yuchcallerlocalResource.PHONE_CONFIG_ADVANCE);
	}
	
	private String getAboutSwitchLabelText(){
		return (m_aboutTextFieldNull.getManager() != null || m_aboutTextField == null)?
					("(+)" + m_mainApp.m_local.getString(yuchcallerlocalResource.PHONE_CONFIG_ABOUT)):
					"(-)" + m_mainApp.m_local.getString(yuchcallerlocalResource.PHONE_CONFIG_ABOUT);
	}
	
	private String getSyncConfigSwitchLabelText(){
		return (mSyncFieldNull.getManager() != null || mSyncField == null)?
					("(+)" + m_mainApp.m_local.getString(yuchcallerlocalResource.PHONE_CONFIG_SYNC)):
					"(-)" + m_mainApp.m_local.getString(yuchcallerlocalResource.PHONE_CONFIG_SYNC);
	}
	
	//! field change event processing function
	public void fieldChanged(Field field, int context) {
		
		if(FieldChangeListener.PROGRAMMATIC == context){
			return;
		}
		
		if(field == m_locationInfoColor){
			
			// change the color
			m_locationTextColorSample.setColor(fsm_locationCandColor[m_locationInfoColor.getSelectedIndex()]);
			ConfigManager.this.invalidate();
			
		}else if(field == m_locationInfoHeight || field == m_locationBoldFont){
			
			replaceSampleText();
			
		}else if(field == m_searchNumberInput){
			if(m_searchNumberInput.getTextLength() > 0){
				String t_number = m_searchNumberInput.getText();
				m_locationTextColorSample.setText(m_mainApp.searchLocation(t_number));
			}			 
		}else if(field == m_advanceSwitchLabel){
			
			showOrHideAdvanceSetting();
			
		}else if(field == m_showDebugScreenBtn){
			
			m_mainApp.popupDebugInfoScreen();
			
		}else if(field == m_intelSearchInput){
			if(m_intelSearchInput.getTextLength() > 0){
				// search the matched result
				m_mainApp.getDbIndex().fillMatchResult(m_intelSearchInput.getText(), m_matchedList, 50);
				m_addMatchedSNIdx = 0;
				
				refreshMatchedList();
			}else{
				clearMatchedList();
			}
		}else if(m_matchedNextPageLabel == field){
			
			m_addMatchedSNIdx += MatchedLabelPageNum;
			Field t_first = addIntelSearchList(m_addMatchedSNIdx);
			t_first.setFocus();
			
		}else if(m_aboutSwitchLabel == field){
			if(m_aboutTextField == null){
				m_aboutTextField	= new ActiveRichTextField(m_mainApp.m_local.getString(yuchcallerlocalResource.PHONE_CONFIG_ABOUT_DETAIL),EditField.READONLY);
			}
			
			if(m_aboutTextFieldNull.getManager() != null){
				replace(m_aboutTextFieldNull, m_aboutTextField);
			}else{
				replace(m_aboutTextField, m_aboutTextFieldNull);
			}
			
			m_aboutSwitchLabel.setText(getAboutSwitchLabelText());
			
		}else if(mSyncSwitchLabel == field){
			
			if(mSyncField == null){
				mSyncField = new SyncOptionManager(m_mainApp);
			}
			
			if(mSyncField.getManager() == null){
				replace(mSyncFieldNull,mSyncField);
			}else{
				replace(mSyncField,mSyncFieldNull);
			}
			
			mSyncSwitchLabel.setText(getSyncConfigSwitchLabelText());
			
		}else if(mCheckVersionBtn == field){
			
			m_mainApp.enableCheckVersionDialogPrompt();
			m_mainApp.checkVersion();
			
		}else if(field instanceof ClickLabel){
			
			// is intel-search result label
			//
			ClickLabel t_cl = (ClickLabel)field;
			if(t_cl.m_speNumber != null){
				if(Dialog.ask(Dialog.D_YES_NO, m_mainApp.m_local.getString(yuchcallerlocalResource.PHONE_ASK_DIAL_NUMBER) + "\n" + t_cl.toString(), 
				Dialog.NO) == Dialog.YES){
					try{
						YuchCaller.CallPhoneNumber(t_cl.m_speNumber.getDialNumber());
					}catch(Exception ex){
						m_mainApp.DialogAlert("Dial Error:" + ex.getMessage());
					}					
				}
			}
		}
	}
	
	// replace sample text
	private void replaceSampleText(){
		
		// get the height of setting
		int t_height = MathUtilities.clamp(20, getTextFieldNum(m_locationInfoHeight), YuchCallerProp.fsm_maxFontHeight);
		
		// replace sample text
		ColorSampleField t_newField = new ColorSampleField(m_locationTextColorSample.m_color);
		
		t_newField.setFont(m_locationTextColorSample.getFont().derive(
				m_locationTextColorSample.getFont().getStyle() | (m_locationBoldFont.getChecked()?Font.BOLD:0),t_height));
		
		replace(m_locationTextColorSample, t_newField);
		m_locationTextColorSample = t_newField;
	}
	
	
	// save the properties
	public void saveProp(){
		try{

			YuchCallerProp tProp = m_mainApp.getProperties();
			
			if(m_recvVibrationTime != null){
				
				tProp.setRecvPhoneVibrationTime(getTextFieldNum(m_recvVibrationTime));
				tProp.setHangupPhoneVibrationTime(getTextFieldNum(m_hangupVibrationTime));
				
				tProp.setLocationPosition_x(getTextFieldNum(m_locationInfoPosition_x));
				tProp.setLocationPosition_y(getTextFieldNum(m_locationInfoPosition_y));			
				
				tProp.setLocationColor(fsm_locationCandColor[m_locationInfoColor.getSelectedIndex()]);
				tProp.setLocationHeight(MathUtilities.clamp(20, getTextFieldNum(m_locationInfoHeight), YuchCallerProp.fsm_maxFontHeight));
				
				tProp.setBoldFont(m_locationBoldFont.getChecked());
				tProp.setShowPhoneCallTimeLen(mShowPhoneCallTime.getChecked());
				
				boolean tEnableCaller = tProp.isEnableCaller();
				tProp.setEnableCaller(!mDisableLocationInfo.getChecked());
				
				if(tEnableCaller != tProp.isEnableCaller()){
					m_mainApp.initDisplayLocation(false);
				}				
				
				boolean t_initMenu = false;
				
				if(!m_IPDialPrefix.getText().equals(tProp.getIPDialNumber())){
					tProp.setIPDialNumber(m_IPDialPrefix.getText());
					t_initMenu = true;
				}			
				
				if(m_showSystemMenu.getChecked() != tProp.showSystemMenu()){
					tProp.setShowSystemMenu(m_showSystemMenu.getChecked());
					t_initMenu = true;
				}
				
				if(t_initMenu){
					m_mainApp.initMenus(false);
				}
			}
			
			
			if(mSyncField != null && mSyncField.isDirty()){
				mSyncField.fetchSyncProp(false);
			}
			
			tProp.save();
			
			// notify the yuchcaller to change style of text font
			m_mainApp.changeLocationTextFont();
			
		}catch(Exception ex){
			m_mainApp.SetErrorString("CMSP",ex);
			m_mainApp.DialogAlert("Error! " + ex.getMessage());
		}
	}
	
	// get the text field string and convert it to number 
	public int getTextFieldNum(TextField _text){
		if(_text.getTextLength() == 0){
			return 0;
		}
		
		try{
			return Integer.parseInt(_text.getText());
		}catch(Exception ex){
			m_mainApp.SetErrorString("GTFN",ex);
		}
		
		return 0;
	}
		
	//! main screen is discarded
	protected void onUndisplay(){
		super.onUndisplay();
		
		if(this == m_mainApp.m_configManager){
			m_mainApp.m_configManager = null;
		}		
	}
	
	//! refresh the Matched List 
	private void refreshMatchedList(){
		
		clearMatchedList();
		
		// add the label
		addIntelSearchList(m_addMatchedSNIdx);
	}
	
	//! add a part of list
	private Field addIntelSearchList(int _begin){
		
		// delete former m_matchedNextPageLabel first
		if(m_matchedNextPageLabel != null 
		&& m_matchedNextPageLabel.getManager() != null){
			m_intelSearchListMgr.delete(m_matchedNextPageLabel);
		}	
		
		// get the min page size end number
		int t_end = Math.min(_begin + MatchedLabelPageNum, m_matchedList.size());
		Field t_first = null;
		for(int i = _begin;i < t_end;i++){
			SpecialNumber t_sn = (SpecialNumber)m_matchedList.elementAt(i);
			
			Field t_clickedField = allocLabelField(t_sn);
			
			if(t_first == null){
				t_first = t_clickedField;
			}
			m_intelSearchListMgr.add(t_clickedField);
		}
		
		if(t_end != m_matchedList.size()){
			if(m_matchedNextPageLabel == null){
				m_matchedNextPageLabel = new ClickLabel(m_mainApp.m_local.getString(yuchcallerlocalResource.PHONE_CONFIG_INTEL_SEARCH_MORE));
				m_matchedNextPageLabel.setChangeListener(this);
			}
			
			m_intelSearchListMgr.add(m_matchedNextPageLabel);
		}
		
		return t_first;
	}
		
	//! clear the matched list
	private void clearMatchedList(){
		
		// release all field
		int t_fieldCount = m_intelSearchListMgr.getFieldCount();
		for(int i = 0;i < t_fieldCount;i++){
			
			Field t_field = m_intelSearchListMgr.getField(i);
			
			if(t_field != m_matchedNextPageLabel){
				// except matched next page label to restore
				//
				m_allocList.addElement(m_intelSearchListMgr.getField(i));
			}
		}
		
		m_intelSearchListMgr.deleteAll();
	}
	
	//! allocate label field
	private ClickLabel allocLabelField(SpecialNumber _sn){
		ClickLabel t_label;
		if(!m_allocList.isEmpty()){
			int t_idx = m_allocList.size() - 1;
			
			t_label = (ClickLabel)m_allocList.elementAt(t_idx);
			t_label.setText(_sn.toString());
			
			m_allocList.removeElementAt(t_idx);
		}else{
			t_label = new ClickLabel(_sn.toString());
			t_label.setChangeListener(this);
		}
		
		t_label.m_speNumber = _sn;
		return t_label;
	}
	
	/**
	 * refresh the sync prompt 
	 */
	public void refreshSyncPrompt(){		
		if(getScreen() != null && getScreen().getApplication() != null){
			
			getScreen().getApplication().invokeLater(new Runnable() {
				
				public void run() {
					refreshSyncPromptImpl();					
				}
			});
		}
	}
	
	/**
	 * report text font
	 */
	private Font mReportTextFont = Font.getDefault().derive(Font.getDefault().getStyle(),Font.getDefault().getHeight() - 2);
	
	/**
	 * refresh sync prompt implement
	 */
	private void refreshSyncPromptImpl(){
		
		if(m_mainApp.getSyncMain() == null){
			return;
		}
		
		String[] tErrorStr	= m_mainApp.getSyncMain().mErrorStr;
		String[] tInfoStr	= m_mainApp.getSyncMain().mInfoStr;
		
		StringBuffer sb = new StringBuffer();
		
		for(int i = 0 ; i < tErrorStr.length;i++){
			String e = tErrorStr[i];
			
			if(e != null){
				sb.append(e).append("\n");
			}
		}
		
		if(mSyncErrorTextField == null){
			mSyncErrorTextField = new ColorActiveRichTextField(sb.toString(),EditField.READONLY | EditField.NON_FOCUSABLE,0xd20005);
			mSyncErrorTextField.setFont(mReportTextFont);
			
			add(mSyncErrorTextField);
		}else{
			mSyncErrorTextField.setText(sb.toString());
		}
		
		// refresh the information prompt
		sb = new StringBuffer();
		for(int i = 0 ; i < tInfoStr.length;i++){
			String e = tInfoStr[i];
			
			if(e != null){
				sb.append(e).append("\n");
			}
		}
		
		if(mSyncInfoTextField == null){
			mSyncInfoTextField = new ActiveRichTextField(sb.toString(),EditField.READONLY | EditField.NON_FOCUSABLE);
			mSyncInfoTextField.setFont(mReportTextFont);
			
			add(mSyncInfoTextField);
		}else{
			mSyncInfoTextField.setText(sb.toString());
		}
	}
	
	/**
	 * escape key press return or clear state
	 * @return
	 */
	public boolean escapeKeyPress(){
		
		boolean t_escaped = true;
		
		if(m_intelSearchInput.isFocus() && m_intelSearchInput.getTextLength() > 0){
			m_intelSearchInput.setText("");
			clearMatchedList();
			t_escaped = false;
		}
		
		if(m_searchNumberInput.isFocus() && m_searchNumberInput.getTextLength() > 0){
			m_searchNumberInput.setText("");
			fieldChanged(m_searchNumberInput,0);
			t_escaped = false;
		}
		
		if(m_aboutTextFieldNull.getManager() == null){
			fieldChanged(m_aboutSwitchLabel,0);
			t_escaped = false;		
		}
		
		if(m_advanceManagerNull.getManager() == null){
			if(!m_advanceManager.isDirty()){
				fieldChanged(m_advanceSwitchLabel, 0);
				t_escaped = false;
			}
		}
		
		if(mSyncFieldNull.getManager() == null){
			if(!mSyncField.isDirty()){
				fieldChanged(mSyncSwitchLabel, 0);
				t_escaped = false;
			}			
		}
		
		return t_escaped;
	}
	
	
	/**
	 * color sample field 
	 * @author tzz
	 *
	 */
	final class ColorSampleField extends LabelField{

		int m_color;
		
		public ColorSampleField(int _color){
			super(m_mainApp.m_local.getString(yuchcallerlocalResource.PHONE_CALL_TEXT_COLOR_SAMPLE),Field.NON_FOCUSABLE);
			m_color = _color;
		}
		
		public void setColor(int _color){
			m_color = _color;
		}
				
		protected void paint(Graphics graphics) {
			int t_color = graphics.getColor();
			try{
				graphics.setColor(m_color);
				super.paint(graphics);
			}finally{
				graphics.setColor(t_color);
			}
		}
	}
	
	/**
	 * color field to show the choice field
	 * @author tzz
	 *
	 */
	final class ColorChoiceField{

		int m_color;
		
		public ColorChoiceField(int _color){
			m_color = _color;
		}
				
		public String toString(){
			switch(m_color){
			case 0:
				return m_mainApp.m_local.getString(yuchcallerlocalResource.PHONE_CALL_TEXT_COLOR_BLACK);
			case 0xffffff:
				return m_mainApp.m_local.getString(yuchcallerlocalResource.PHONE_CALL_TEXT_COLOR_WHITE);
			default:
				for(int i = 0;i < fsm_locationCandColor.length;i++){
					if(fsm_locationCandColor[i] == m_color){
						return m_mainApp.m_local.getString(fsm_locationCandColorStr[i]);
					}
				}
				
				return m_mainApp.m_local.getString(yuchcallerlocalResource.PHONE_CALL_TEXT_COLOR_DUMMY) + " #" + Integer.toHexString(m_color);	
			}
		}
	}
	
	/**
	 * Input edit field with prompt text when unfocus and never dirty
	 * @author tzz
	 *
	 */
	final class InputEditField extends AutoTextEditField{
		final String m_unfocusPrompt;
		
		// unfocus
		public InputEditField(String _prompt,long _style){
			super("","",11,EditField.NO_NEWLINE | _style);
			m_unfocusPrompt		= _prompt;
		}
		
		// never dirty to prompt save
		public boolean isDirty() {
			return false;
		}
		
		protected void onFocus(int direction){
			super.onFocus(direction);
			invalidate();
		}
		
		protected void onUnfocus(){
			super.onUnfocus();
			invalidate();
		}
		
		// paint the prompt text when unfocus
		public void paint(Graphics _g){
			
			if(!isFocus() && getTextLength() == 0	// is not focus and text is zero 
			&& m_unfocusPrompt != null && m_unfocusPrompt.length() > 0){	// unfocusPrompt is valuable
				
				int t_color = _g.getColor();
				try{
					_g.setColor(0xb0b2b0);
					_g.drawText(m_unfocusPrompt, 3, 0);
				}finally{
					_g.setColor(t_color);
				}
			}
			
			super.paint(_g);
		}
	}
	
	//! the intelSearchListMgr
	final class SearchResultListMgr extends VerticalFieldManager{
		
		public SearchResultListMgr(){
			super(Manager.NO_HORIZONTAL_SCROLL);
		}
	}
	
	/**
	 * ClickLabel for some label
	 * 
	 * @author tzz
	 *
	 */
	final static class ClickLabel extends LabelField{
	
		public SpecialNumber m_speNumber = null;
		
		public ClickLabel(String _label){
			super(_label,Field.FOCUSABLE);
		}
		
		public boolean isDirty(){
			return false;
		}
		
		protected boolean keyChar( char character, int status, int time){
	        if( character == Characters.ENTER || character == Characters.SPACE) {
	            fieldChangeNotify( 0 );
	            return true;
	        }
	        return super.keyChar( character, status, time );
	    }

	    protected boolean navigationClick( int status, int time ){ 
	        keyChar(Characters.ENTER, status, time );            
	        return true;
	    }		
	}
	
	/**
	 * active rich textfield with color
	 * @author tzz
	 *
	 */
	final static class ColorActiveRichTextField extends ActiveRichTextField{
		
		/**
		 * foreground color
		 */
		private int mColor;
		
		public ColorActiveRichTextField(String text,long style,int color){
			super(text,style);
			
			mColor = color;
		}
		
		protected void paint(Graphics graphics) {
			int color = graphics.getColor();
			try{
				graphics.setColor(mColor);
				super.paint(graphics);
			}finally{
				graphics.setColor(color);
			}
		}
		
	}

}
