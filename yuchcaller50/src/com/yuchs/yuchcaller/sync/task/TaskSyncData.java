package com.yuchs.yuchcaller.sync.task;

import javax.microedition.pim.PIMItem;

import net.rim.blackberry.api.pdap.BlackBerryToDo;

import com.yuchs.yuchcaller.sync.AbsData;
import com.yuchs.yuchcaller.sync.AbsSyncData;

public class TaskSyncData extends AbsSyncData {

	public TaskData getData(){return (TaskData)m_data;}
	public void setData(TaskData d){m_data = d;}
		
	public void exportData(PIMItem item) throws Exception {
		BlackBerryToDo todo = (BlackBerryToDo)item;
		
		setIntField(todo, BlackBerryToDo.STATUS, getData().status);
		setStringField(todo, BlackBerryToDo.SUMMARY, getData().summary);
		setStringField(todo, BlackBerryToDo.NOTE, getData().note);
		setBooleanField(todo, BlackBerryToDo.COMPLETED, getData().status == TaskData.STATUS_COMPLETED);
		setDateField(todo, BlackBerryToDo.DUE, getData().deadline);
	}

	public void importData(PIMItem item) throws Exception {
		
		BlackBerryToDo todo = (BlackBerryToDo)item;
		
		if(getData() == null){
			setData(new TaskData());
		}else{
			getData().clear();
		}		

		int[] fieldIds = todo.getFields();
		int id;
		for(int i = 0;i < fieldIds.length;i++){
			id = fieldIds[i];
			
			switch(id){
			case BlackBerryToDo.UID:
				setBBID(getStringField(todo,id));
				break;
			case BlackBerryToDo.STATUS:
				getData().status = getIntField(todo, id);
				break;
			case BlackBerryToDo.SUMMARY:
				getData().summary = getStringField(todo,id);
				break;
			case BlackBerryToDo.NOTE:
				getData().note = getStringField(todo,id);
				break;
			case BlackBerryToDo.DUE:
				getData().deadline = getDateField(todo,id);
				break;
			}
		}
	}

	/**
	 * need calculate md5 by minTime and by index
	 * @param minTime
	 * @param idx  index of current AbsSyncData
	 * @return
	 */
	protected boolean needCalculateMD5(long minTime,int idx){
		if(getData() != null){			
			return idx < TaskData.MAX_SYNC_TASK;
		}
		
		return false;
	}

	protected AbsData newData() {
		return new TaskData();
	}

}
