package org.gradle.needle.util;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;
import org.gradle.needle.dbo.GlobalSettings;

public class OracleConnAndDataAssert {
	private Connection conn=null;   //���ݿ����Ӷ���
	private Statement ps=null;  //���ݿ�Ԥ�������
	private ResultSet rs=null;   //��ѯ�����
	private static Logger logger = Logger.getLogger(OracleConnAndDataAssert.class
			.getName());
	
	//��ȡ���ݿ�����
	public Connection GetConn() {
		try{
			Class.forName(GlobalSettings.driver);
		}catch(ClassNotFoundException e){
			logger.info("��������ʧ�ܣ�"+ e.getMessage());
		}
		
		try{
			conn = DriverManager.getConnection(GlobalSettings.connurl, GlobalSettings.userName, GlobalSettings.password);
			 if(conn != null && !conn.isClosed())
				 logger.info("���ݿ����ӳɹ���");
		}catch(SQLException e){  
			e.printStackTrace();
		}
		return conn;
	}
	
	//�ر���������
	public void ConnClose(){
		if (rs!=null)
			try{
				rs.close();
			}catch(SQLException e){
				e.printStackTrace();  
			}
		
		if (ps!=null)
			try{
				ps.close();
			}catch(SQLException e){
				e.printStackTrace(); 
			}
		
		if (conn!=null)
			try{
				conn.close();
			}catch(SQLException e){
				e.printStackTrace(); 
			}
		logger.info("���ݿ������ѹر�");
	}
	
	//��ȡ������ѯ���
	public ResultSet Query(String sql){
		conn = GetConn();
		try{
			ps = conn.createStatement();
			rs = ps.executeQuery(sql);
			System.out.println("��ѯ�ɹ���");
		}catch (SQLException e){
			e.printStackTrace(); 
		}
		return rs;
	}
   
	//DataAssert()����1����ȡ�������ֶεĲ�ѯ�����������ֵ�Աȣ�������֤���
	public void DataAssert(String sql, String qycolumn, String etresult){
		String qyresult = null;
		try{
			ps = conn.createStatement();
			rs = ps.executeQuery(sql);
			while (rs.next()){
				qyresult = rs.getString(qycolumn);
			}
			logger.info("���ݲ�ѯ�ɹ���");
		}catch (SQLException e){
			e.printStackTrace(); 
		}
		if(qyresult.equals(etresult))
			logger.info("������֤ͨ��");
		else
			logger.info("������֤��ͨ��");
	}
	
	//DataAssert()����2����ȡ���������ֶεĽ�����жԱ�
	public void DataAssert(String zbsql,String zbcolumn,String bbsql,String bbcolumn){
		double zbresultf = 0;
		double bbresultf = 0;
		
		try{
			ps = conn.createStatement();
			ResultSet zbrs = ps.executeQuery(zbsql);
			while (zbrs.next()){
				double zbresult = zbrs.getFloat(zbcolumn);
				BigDecimal  bd1 = new BigDecimal(zbresult);  
				zbresultf = bd1.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();  
			}	
		}catch (SQLException e){
			e.printStackTrace(); 
		}
		
		try{
			ps = conn.createStatement();
			ResultSet bbrs = ps.executeQuery(bbsql);
				while (bbrs.next()){
					double bbresult = bbrs.getFloat(bbcolumn);
					BigDecimal  bd2 = new BigDecimal(bbresult);  
					bbresultf = bd2.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();  
				}
		}catch (SQLException e){
			e.printStackTrace(); 
		}
		
		if(bbresultf == zbresultf)
			logger.info(bbcolumn + "----------" + "passed");
		else
			logger.info(bbcolumn + "---------- " + "failed");
	}
	
	//DataAssert()����3,��ȡ�������������ݵĹ����Ա�
	public void DataAssert(String key, String zbsql,String zbcolumn,String bbsql,String bbcolumn){
		 Map<String,Float> zbmap = new HashMap<String,Float>();
		 Map<String,Float> bbmap = new HashMap<String,Float>();
		 try{
				ps = conn.createStatement();
				ResultSet zbrs = ps.executeQuery(zbsql);
				 while (zbrs.next()){
							zbmap.put(zbrs.getString(key),zbrs.getFloat(zbcolumn));
				 }
			}catch (SQLException e){
				e.printStackTrace(); 
			}
		 
		 try{
				ps = conn.createStatement();
				ResultSet bbrs = ps.executeQuery(bbsql);
				 while (bbrs.next()){
							bbmap.put(bbrs.getString(key),bbrs.getFloat(bbcolumn));
				 }
			}catch (SQLException e){
				e.printStackTrace(); 
			}
		 
		 Iterator<String> i =zbmap.keySet().iterator();
		 while(i.hasNext()){
			 String j = i.next();
			 double one = zbmap.get(j);
			 BigDecimal  bd1 = new BigDecimal(one);  
			 double onef = bd1.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();  
			 double two = bbmap.get(j);
			 BigDecimal  bd2 = new BigDecimal(two);  
			 double twof = bd2.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue(); 
			 if (onef == twof)
				 logger.info(j + "----------" + bbcolumn + "----------passed");
			 else
				 logger.info(j + "----------" + bbcolumn + "----------failed"); 
		 }
	}
}
