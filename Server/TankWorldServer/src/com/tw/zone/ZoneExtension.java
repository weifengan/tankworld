package com.tw.zone;

import java.io.IOException;

import javax.servlet.jsp.jstl.core.Config;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.smartfoxserver.v2.SmartFoxServer;
import com.smartfoxserver.v2.api.CreateRoomSettings;
import com.smartfoxserver.v2.api.CreateRoomSettings.RoomExtensionSettings;
import com.smartfoxserver.v2.config.ZoneSettings.ExtensionSettings;
import com.smartfoxserver.v2.config.ZoneSettings.RoomSettings;
import com.smartfoxserver.v2.core.SFSEventType;
import com.smartfoxserver.v2.entities.SFSRoomRemoveMode;
import com.smartfoxserver.v2.exceptions.SFSCreateRoomException;
import com.smartfoxserver.v2.extensions.SFSExtension;
import com.tw.lobby.ExtJoinGameHandler;

import utlis.DBManager;
import utlis.Tools;

public class ZoneExtension extends SFSExtension {

	public static DBManager db;
	public void init() {
		Tools.Init(this);
		//初始化数据库连接
		//初始化数据库
		db=DBManager.GetInstance();
		this.addEventHandler(SFSEventType.USER_LOGIN, SFSUserLoginHandler.class);
		this.addEventHandler(SFSEventType.USER_JOIN_ZONE, SFSUserJoinZoneHandler.class);
		//添加用户登录处理
		this.addEventHandler(SFSEventType.USER_LOGIN, SFSUserLoginHandler.class);
	    this.addEventHandler(SFSEventType.USER_JOIN_ZONE, SFSUserJoinZoneHandler.class);
	    //重呢称检测
	    this.addRequestHandler(ExtType.UpdateRole, ExtUpdateRoleHandler.class);
	    this.addRequestHandler(ExtType.FetchBoardList, ExtFetchBoardListHandler.class);
 
	    
		trace("坦克服务器启动成功。。。。。");
       
		
	}


}
