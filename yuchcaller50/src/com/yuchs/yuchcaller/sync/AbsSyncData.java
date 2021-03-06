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
package com.yuchs.yuchcaller.sync;

import java.io.InputStream;
import java.io.OutputStream;

import javax.microedition.pim.PIMItem;

import com.yuchs.yuchcaller.sendReceive;

public abstract class AbsSyncData {
	
	// the bb system calendar UID
	protected String bID = null;
	
	// the google calendar UID
	protected String gID = null;
	
	// last modified time
	protected long lastMod = 0;
	
	// calendar/contact/task data
	protected AbsData	m_data = null;
	
	public void setBBID(String _bID){bID = _bID;}
	public String getBBID(){return bID;}
	
	public void setGID(String _id){gID = _id;}
	public String getGID(){	return gID;}
	
	public void setLastMod(long _mod){lastMod = _mod;}
	public long getLastMod(){return lastMod;}
	
	/**
	 * new a data class
	 * @return
	 */
	protected abstract AbsData newData();
	
	/**
	 * need calculate md5 by minTime and by index
	 * @param minTime
	 * @param idx  index of current AbsSyncData
	 * @return
	 */
	protected abstract boolean needCalculateMD5(long minTime,int idx);
	
	/**
	 * ouput data to the stream
	 * @param _os
	 * @param _outputData
	 * @throws Exception
	 */
	public void output(OutputStream os,boolean _outputData)throws Exception{
		sendReceive.WriteString(os,getBBID());
		sendReceive.WriteString(os,getGID());
		sendReceive.WriteLong(os,getLastMod());
		
		if(m_data != null && _outputData){
			sendReceive.WriteBoolean(os, true);
			m_data.outputData(os);
		}else{
			sendReceive.WriteBoolean(os, false);
		}
	}
	
	/**
	 * input data from the stream
	 * @param _in
	 * @throws Exception
	 */
	public void input(InputStream in)throws Exception{
		setBBID(sendReceive.ReadString(in));
		setGID(sendReceive.ReadString(in));
		setLastMod(sendReceive.ReadLong(in));
		
		boolean tHasData = sendReceive.ReadBoolean(in);
		if(tHasData){

			if(m_data == null){
				m_data = newData();
			}
			
			m_data.inputData(in);
		}
	}
	
	/**
	 * import blackberry PIM list data
	 * @param event
	 * @param list		EventList
	 */
	public abstract void importData(PIMItem _item)throws Exception;
	
	/**
	 * export the blackberry PIM item to the blackberry
	 * @param event
	 * @throws Exception
	 */
	public abstract void exportData(PIMItem _item)throws Exception;
	
	
	/**
	 * clear the PIM item Fields by id
	 * @param _id
	 * @throws Exception
	 */
	public static void clearPIMItemFields(PIMItem _item,int _id)throws Exception{
		for(int i = 0;i < _item.countValues(_id);i++){
			try{
				_item.removeValue(_id, i);
				i--;
			}catch(Exception e){}
		}
	}
	
	/**
	 * get the PIMItem string without index 
	 * @param _item
	 * @param _id
	 * @return
	 */
	public static String getStringField(PIMItem _item,int _id){
		return getStringField(_item,_id,0);
	}
	
	/**
	 * get the PIMItem string by attribute
	 * @param _item
	 * @param _id
	 * @param _index
	 * @return
	 */
	public static String getStringField(PIMItem _item,int _id,int _index){
		int tCount = _item.countValues(_id);
		if(tCount > 0){
			return _item.getString(_id, _index);
		}
		
		return "";
	}
	
	/**
	 * set the PIMItem id by string without attribute
	 * @param _item
	 * @param _id
	 * @param _value
	 */
	public static void setStringField(PIMItem _item,int _id,String _value){
		setStringField(_item,_id,PIMItem.ATTR_NONE,_value);
	}
	
	/**
	 * set string field by the id attribute
	 * @param _item
	 * @param _id
	 * @param _attr
	 * @param _value
	 */
	public static void setStringField(PIMItem _item,int _id,int _attr,String _value){
		setStringField(_item,_id,_attr,-1,_value);
	}
	
	/**
	 * set string field by the id,attribute,index
	 * Contact.EMAIL can't record the attribute
	 * @param _item
	 * @param _id
	 * @param _attr
	 * @param _index
	 * @param _value
	 */
	public static void setStringField(PIMItem _item,int _id,int _attr,int _index,String _value){
		
		if(_item.getPIMList().isSupportedField(_id)){
			
			int count = _item.countValues(_id);
			if(count > 0){
				
				for(int i = 0;i < count;i++){
					int a = _item.getAttributes(_id, i);
					if(a == _attr || _index == i){
						
						if(_value != null && _value.length() > 0){
							_item.setString(_id,i,_attr,_value);
						}else{
							_item.removeValue(_id,i);
						}
						
						// found return
						return;
					}
				}	
			}
			
			if(_value != null && _value.length() > 0){
				
				if(_item.getPIMList().maxValues(_id) > count){
					_item.addString(_id,_attr,_value);
				}				
			}
		}		
	}
	
	/**
	 * get the the long(for date) field without attribute
	 * @param _item
	 * @param _id
	 * @return
	 */
	public static long getDateField(PIMItem _item,int _id){
		if(_item.getPIMList().isSupportedField(_id)){

			int tCount = _item.countValues(_id);
			if(tCount > 0){
				return _item.getDate(_id, PIMItem.ATTR_NONE);
			}	
		}
		
		return 0;
	}
	
	
	/**
	 * set the date field for PIMItem
	 * @param _item
	 * @param _id
	 * @param _value
	 */
	public static void setDateField(PIMItem _item,int _id,long _value){
		if(_item.getPIMList().isSupportedField(_id) && _value > 0){
			
			if(_item.countValues(_id) > 0){
				_item.setDate(_id,0,PIMItem.ATTR_NONE,_value);
			}else{
				_item.addDate(_id,PIMItem.ATTR_NONE,_value);
			}
			
		}	
	}
		
	/**
	 * set the date field for event by data value
	 * @param _item
	 * @param _id
	 * @param _value
	 */
	public static void setDateField(PIMItem _item,int _id,String _value){
		
		try{
			long v = Long.parseLong(_value);
			setDateField(_item,_id,v);
		}catch(Exception e){}
	}
	
	
	/**
	 * get the integer field for the event
	 * @param _item
	 * @param _id
	 * @return
	 */
	public static int getIntField(PIMItem _item,int _id){
		int tCount = _item.countValues(_id);
		if(tCount > 0){
			return _item.getInt(_id, 0);
		}
		
		return 0;
	}
	
	/**
	 * set the int value of this event
	 * @param _item
	 * @param _id
	 * @param _value
	 */
	public static void setIntField(PIMItem _item,int _id,int _value){
		if(_item.getPIMList().isSupportedField(_id)){
			if(_item.countValues(_id) > 0){
				_item.setInt(_id,0,PIMItem.ATTR_NONE,_value);
			}else{
				_item.addInt(_id,PIMItem.ATTR_NONE,_value);
			}
		}
	}
	
	/**
	 * set the int field by int string
	 * @param _item
	 * @param _id
	 * @param _value
	 */
	public static void setIntField(PIMItem _item,int _id,String _value){
		
		try{
			int v = Integer.parseInt(_value);
			setIntField(_item,_id,v);
		}catch(Exception e){}
	}
	
	/**
	 * get the boolean field for the event
	 * @param _item
	 * @param _id
	 * @return
	 */
	public static boolean getBooleanField(PIMItem _item,int _id){
		int tCount = _item.countValues(_id);
		if(tCount > 0){
			return _item.getBoolean(_id, 0);
		}
		
		return false;
	}
	
	/**
	 * set the boolean field
	 * @param _list
	 * @param _item
	 * @param _id
	 * @param _value
	 */
	public static void setBooleanField(PIMItem _item,int _id,boolean _value){
		if(_item.getPIMList().isSupportedField(_id)){
			if(_item.countValues(_id) > 0){
				_item.setBoolean(_id, 0, PIMItem.ATTR_NONE, _value);
			}else{
				_item.addBoolean(_id, PIMItem.ATTR_NONE, _value);
			}
		}	
	}
	
	/**
	 * get the string array field without 
	 * @param _item
	 * @param _id
	 * @param _resultSize
	 * @return
	 */
	public static String[] getStringArrayField(PIMItem _item,int _id,int _resultSize){
		return getStringArrayField(_item,_id,PIMItem.ATTR_NONE,_resultSize);
	}
	
	/**
	 * get the string array field by attribute
	 * @param _item
	 * @param _id
	 * @param _attr
	 * @param _resultSize
	 * @return
	 */
	public static String[] getStringArrayField(PIMItem _item,int _id,int _attr,int _resultSize){
		
		String[] result = null;
		
		int tCount = _item.countValues(_id);
		if(tCount > 0){
			if(_item.getPIMList().getFieldDataType(_id) == PIMItem.STRING_ARRAY){
				
				for(int i = 0; i < tCount;i++){
					// spcific attribute
					if(_item.getAttributes(_id, i) == _attr){
						result = _item.getStringArray(_id, i);
					}
				}
								
			}else if(_item.getPIMList().getFieldDataType(_id) == PIMItem.STRING){
						
				result = new String[tCount];
				for(int i = 0 ;i < tCount;i++){
					result[i] = _item.getString(_id, i);
				}				
			}
		}
		
		// resize the result if not equal
		if(result != null && result.length != _resultSize && _resultSize > 0){
			
			String[] tmp = new String[_resultSize];
			
			int minSize = Math.min(result.length, _resultSize);
			for(int i = 0;i < minSize;i++){
				tmp[i] = result[i];
			}
			
			result = tmp;
		}
		
		return result;
	}
	
	/**
	 * set the field array field without attribute
	 * @param _item
	 * @param _id
	 * @param _value
	 */
	public static void setStringArrayField(PIMItem _item,int _id,String[] _value){
		setStringArrayField(_item,_id,PIMItem.ATTR_NONE,_value);
	}
	
	/**
	 * set the String array field
	 * @param _item
	 * @param _id
	 * @param _attr
	 * @param _value
	 */
	public static void setStringArrayField(PIMItem _item,int _id,int _attr,String[] _value){
		if(_item.getPIMList().isSupportedField(_id)){
			
			int count = _item.countValues(_id);
			
			if(_item.getPIMList().getFieldDataType(_id) == PIMItem.STRING_ARRAY){

				if(count > 0){
					
					for(int i = 0;i < count;i++){
						int a = _item.getAttributes(_id, i);
						if(a == _attr){
							
							if(_value != null && _value.length > 0){
								_item.setStringArray(_id, i, _attr, _value);
							}else{
								_item.removeValue(_id,i);
							}
							
							return;
						}
					}				
				}
				
				if(_value != null && _value.length > 0){
					if(_item.getPIMList().maxValues(_id) > count){
						_item.addStringArray(_id, _attr, _value);
					}
					
				}
				
			}else if(_item.getPIMList().getFieldDataType(_id) == PIMItem.STRING){
				
				if(count > 0){
					for(int i = 0;i < count;i++){
						_item.removeValue(_id,0);
					}
				}
				
				if(_value != null && _value.length > 0){
					int addCount = Math.min(_value.length,_item.getPIMList().maxValues(_id));
					for(int i = 0 ;i < addCount;i++){
						_item.addString(_id, _attr, _value[i]);
					}
				}
			}
		}
	}
}
