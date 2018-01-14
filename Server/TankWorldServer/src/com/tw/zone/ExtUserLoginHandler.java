package com.tw.zone;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.smartfoxserver.bitswarm.sessions.Session;
import com.smartfoxserver.v2.core.ISFSEvent;
import com.smartfoxserver.v2.core.SFSEventParam;
import com.smartfoxserver.v2.entities.Room;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.entities.variables.SFSUserVariable;
import com.smartfoxserver.v2.entities.variables.UserVariable;
import com.smartfoxserver.v2.exceptions.SFSErrorCode;
import com.smartfoxserver.v2.exceptions.SFSErrorData;
import com.smartfoxserver.v2.exceptions.SFSException;
import com.smartfoxserver.v2.exceptions.SFSLoginException;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;
import com.smartfoxserver.v2.extensions.BaseServerEventHandler;

import utlis.DBManager;

public class ExtUserLoginHandler extends BaseClientRequestHandler {

	@Override
	public void handleClientRequest(User arg0, ISFSObject arg1) {
		// TODO Auto-generated method stub
		//获取用户名和密码
		String name=arg1.getUtfString("username");
		String pwd=arg1.getUtfString("password");
		SFSObject resData=new SFSObject();
		//使用数据库根据用户名进行查询
		
		List<HashMap> accounts=DBManager.GetInstance().doQuery("select * from account where acc_name='"+name+"'");
	
    	//判断是否查询到记录
		if(accounts.size()<1) {
			resData.putBool("success", false);
			resData.putUtfString("username", name);
			resData.putUtfString("password",pwd);
			
			resData.putUtfString("info", "此账户不存在!");
			this.send(ExtType.UserLogin,resData, arg0);
		}else {
			//取出查到的账户
			HashMap user=accounts.get(0);
			if( user.get("acc_pwd").toString().equals(pwd)) {
				
				resData.putBool("success", true);
				resData.putUtfString("username", name);
				resData.putUtfString("password",pwd);
				resData.putUtfString("nick",user.get("acc_nick").toString());
				resData.putInt("role", (int)user.get("acc_role"));
				resData.putInt("coin",(int)user.get("acc_coin"));
				resData.putInt("diamond",(int)user.get("acc_diamond"));
				resData.putUtfString("info", "登陆成功!");
				arg0.isPlayer();
				arg0.isLocal();
				arg0.setName(name);
				Room roleRoom=this.getParentExtension().getParentZone().getRoomByName("roleroom");
				Room lobby=this.getParentExtension().getParentZone().getRoomByName("lobby");
				try {
					if(resData.getUtfString("nick").length()==0) {
						trace("用户"+arg0+"登录RoleRoom成功!");
						
						//roleRoom.addUser(arg0);
						this.getApi().joinRoom(arg0, roleRoom);
					}else {
						
						//在自身存储角色和呢称
						SFSUserVariable surole=new SFSUserVariable("role",resData.getInt("role"));
						SFSUserVariable sunick=new SFSUserVariable("nick",resData.getUtfString("nick"));
						List<UserVariable> uvs= new ArrayList();
						uvs.add(surole);
						uvs.add(sunick);
						arg0.setVariables(uvs);
						
						trace("用户"+arg0+"登录Lobby成功!");
						//lobby.addUser(arg0);
						this.getApi().joinRoom(arg0, lobby);
					}
					
					
					
				}catch(Exception e) {
					trace("用户进入 房间失败");
				}
				this.send(ExtType.UserLogin,resData, arg0);
			}else {
				resData.putBool("success", false);
				resData.putUtfString("username", name);
				resData.putUtfString("password",pwd);
				resData.putUtfString("info", "账号或密码错误!");
				this.send(ExtType.UserLogin,resData, arg0);
			}
		}
	
	}

	

}
