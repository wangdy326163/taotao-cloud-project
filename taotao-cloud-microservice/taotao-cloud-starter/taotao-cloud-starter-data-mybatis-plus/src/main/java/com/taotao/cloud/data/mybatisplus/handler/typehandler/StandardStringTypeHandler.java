package com.taotao.cloud.data.mybatisplus.handler.typehandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.ibatis.executor.result.ResultMapException;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

/**
 * 自定义mybatis处理类，将null返回为空串（‘’）
 *
 * <pre class="code">
 *      支付通道信息列表
 *      {@code @see PayChannelInfo}
 *
 *      {@code @TableField(typeHandler} = StandardStringTypeHandler.class)
 *      {@code @BigField}
 *      private List<PayChannelInfo> payChannelInfo;
 * </pre>
 */
@MappedTypes({String.class})
@MappedJdbcTypes(JdbcType.VARCHAR)
public class StandardStringTypeHandler extends BaseTypeHandler<String> {


	@Override
	public String getResult(ResultSet rs, String columnName) {
		String result;
		try {
			result = getNullableResult(rs, columnName);
		} catch (Exception e) {
			throw new ResultMapException(
				"Error attempting to get column '" + columnName + "' from result set.  Cause: " + e,
				e);
		}
		return result;
	}

	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, String parameter,
		JdbcType jdbcType)
		throws SQLException {
		ps.setString(i, parameter);
	}

	@Override
	public String getNullableResult(ResultSet rs, String columnName)
		throws SQLException {
		return rs.getString(columnName) == null ? "" : rs.getString(columnName);
	}

	@Override
	public String getNullableResult(ResultSet rs, int columnIndex)
		throws SQLException {
		return rs.getString(columnIndex) == null ? "" : rs.getString(columnIndex);
	}

	@Override
	public String getNullableResult(CallableStatement cs, int columnIndex)
		throws SQLException {
		return cs.getString(columnIndex) == null ? "" : cs.getString(columnIndex);
	}
}

