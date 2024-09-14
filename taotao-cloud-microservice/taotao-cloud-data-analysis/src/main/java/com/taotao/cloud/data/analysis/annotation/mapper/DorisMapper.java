package com.taotao.boot.data.analysis.annotation.mapper;

import com.taotao.boot.data.analysis.annotation.Doris;
import org.apache.ibatis.annotations.Mapper;

@Mapper
@Doris
public interface DorisMapper extends BaseMapper<DataInfo> {

    List<DataInfo> selectAll();

}
