package com.demo.dao;


import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.ArrayList;
import java.util.HashMap;


public interface SimilarResultDao {
    @Select("select * from test_question_110 where num =#{num}")
    QAEntity getQAEntity(@Param("num") int num);

    @Select("select bestResponse from test_question_110 where num = #{num}")
    String getAnswer(int num);

    @Select("select questionTitle from test_question_110 where num = #{num}")
    String getTitle(int num);

    @Select("select questionContent from test_question_110 where num = #{num}")
    String getContent(int num);


    @Update("update test_question_110 set dm = #{dm} where num = #{num}")
    void setDm(@Param("num") int num,@Param("dm") int dm);

    @Update("update test_question_110 set vector = #{vector} where num =#{num}")
    void setVector(@Param("num") int num,@Param("vector") String vector);

    @Select("select num from test_question_110 ignore index(dm_index)")
    int[] getNum();

    @Select("select dm from test_question_110 where num = #{num}")
    int getDm(@Param("num") int num);

    @Select("select num from test_question_110 ignore index(dm_index)")
    ArrayList<Integer> getNumList();

    @Select("select vector from test_question_110 ignore index(dm_index)")
    ArrayList<String> getVector();

    @Select("select vector from test_question_110 force index(dm_index) where dm = #{dm} or dm = 0")
    ArrayList<String> getVectorFromDm(@Param("dm") int dm);

    @Select("select num from test_question_110 force index(dm_index) where dm=#{dm} or dm=0")
    ArrayList<Integer> getNumFromDm(@Param("dm") int dm );

    @Select("select vector from test_question_110 where num=#{num}")
    String getVecFromNum(@Param("num") int num);

    @Update("update test_question_110 set sdp_re=#{sdp_re} where num = #{num}")
    void insertSdpRe(@Param("sdp_re") String sdp_re,@Param("num")int num);

    @Select("select sdp_re from test_question_110 where num=#{num}")
    String getSdp_reFromNum(@Param("num") int num );
}
