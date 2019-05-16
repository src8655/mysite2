package com.cafe24.mysite.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.cafe24.mysite.vo.BoardVo;


@Repository
public class BoardDao {
	public boolean delete(Long no) {
		boolean result = false;
		
		Connection con = null;
		PreparedStatement pstmt = null;
		
		try {
			con = getConnection();
			
			String sql = "delete from board"
					+ " where no=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setLong(1, no);
			int count = pstmt.executeUpdate();
			
			if(count == 1) result = true;
			
			
		} catch(SQLException e) {
			System.out.println("error" + e);
		}finally {
			try {
				if(pstmt != null) pstmt.close();
				if(con != null) con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return result;
	}
	public boolean update(BoardVo vo) {
		boolean result = false;
		
		Connection con = null;
		PreparedStatement pstmt = null;
		
		try {
			con = getConnection();
			
			String sql = "update board set"
					+ " subject=?,"
					+ " contents=?"
					+ " where no=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, vo.getSubject());
			pstmt.setString(2, vo.getContents());
			pstmt.setLong(3, vo.getNo());
			int count = pstmt.executeUpdate();
			
			if(count == 1) result = true;
			
			
		} catch(SQLException e) {
			System.out.println("error" + e);
		}finally {
			try {
				if(pstmt != null) pstmt.close();
				if(con != null) con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return result;
	}
	public BoardVo selectOne(Long no) {
		BoardVo result = new BoardVo();
		
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			con = getConnection();
			
			String sql = "select a.no, a.subject, a.hit, date_format(a.write_date, \'%Y-%m-%d\'), b.name, a.contents, b.no" + 
					" from board a, user b"
					+ " where a.user_no=b.no"
					+ " and a.no=?" + 
					" order by a.no desc";
			pstmt = con.prepareStatement(sql);
			pstmt.setLong(1, no);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				result.setNo(rs.getLong(1));
				result.setSubject(rs.getString(2));
				result.setHit(rs.getLong(3));
				result.setWriteDate(rs.getString(4));
				result.setUserName(rs.getString(5));
				result.setContents(rs.getString(6));
				result.setUserNo(rs.getLong(7));
			}
			
			
		} catch(SQLException e) {
			System.out.println("error" + e);
		}finally {
			try {
				if(rs != null) rs.close();
				if(pstmt != null) pstmt.close();
				if(con != null) con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return result;
	}
	public List<BoardVo> getList(String kwd) {
		List<BoardVo> result = new ArrayList<BoardVo>();
		
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			con = getConnection();
			
			String sql = "select a.no, a.subject, a.hit, date_format(a.write_date, \'%Y-%m-%d\'), b.name" + 
					" from board a, user b"
					+ " where a.user_no=b.no"
					+ " and subject like ?" + 
					" order by no desc";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, "%"+kwd+"%");
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				BoardVo vo = new BoardVo();
				vo.setNo(rs.getLong(1));
				vo.setSubject(rs.getString(2));
				vo.setHit(rs.getLong(3));
				vo.setWriteDate(rs.getString(4));
				vo.setUserName(rs.getString(5));
				
				result.add(vo);
			}
			
			
		} catch(SQLException e) {
			System.out.println("error" + e);
		}finally {
			try {
				if(rs != null) rs.close();
				if(pstmt != null) pstmt.close();
				if(con != null) con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return result;
	}
	public boolean insert(BoardVo vo) {
		boolean result = false;
		
		Connection con = null;
		PreparedStatement pstmt = null;
		
		try {
			con = getConnection();
			
			String sql = "insert into board values(null, ?, ?, ?, now(), ?);";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, vo.getSubject());
			pstmt.setString(2, vo.getContents());
			pstmt.setLong(3, vo.getHit());
			pstmt.setLong(4, vo.getUserNo());
			
			int count = pstmt.executeUpdate();
			
			if(count == 1) result = true;
			
		} catch(SQLException e) {
			System.out.println("error" + e);
		}finally {
			try {
				if(pstmt != null) pstmt.close();
				if(con != null) con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return result;
	}
	
	//커넥션 받는 함수
	private Connection getConnection() throws SQLException {
		Connection con = null;
		
		try {
			Class.forName("org.mariadb.jdbc.Driver");
			
			String url = "jdbc:mariadb://192.168.0.10:3307/webdb";
			con = DriverManager.getConnection(url, "webdb", "webdb");
			
		} catch (ClassNotFoundException e) {
			System.out.println("드라이버 로딩 실패 : " + e);
		}
		
		
		return con;
	}
}
