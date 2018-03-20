package com.demo.dao;


import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.ArrayList;
import java.util.HashMap;


public interface SimilarResultDao {
    @Select("select * from ask_question_110 where num =#{num}")
    QAEntity getQAEntity(@Param("num") int num);

    @Select("select bestResponse from ask_question_110 where num = #{num}")
    String getAnswer(int num);

    @Select("select questionTitle from ask_question_110 where num = #{num}")
    String getTitle(int num);

    @Select("select questionContent from ask_question_110 where num = #{num}")
    String getContent(int num);


    @Update("update ask_question_110 set dm = #{dm} where num = #{num}")
    void setDm(@Param("num") int num,@Param("dm") int dm);

    @Update("update ask_question_110 set vector = #{vector} where num =#{num}")
    void setVector(@Param("num") int num,@Param("vector") String vector);

    @Select("select num from ask_question_110 ignore index(dm_index)")
    int[] getNum();

    @Select("select dm from ask_question_110 where num = #{num}")
    int getDm(@Param("num") int num);

    @Select("select num from ask_question_110 ignore index(dm_index)")
    ArrayList<Integer> getNumList();

    @Select("select vector from ask_question_110 ignore index(dm_index)")
    ArrayList<String> getVector();

    @Select("select vector from ask_question_110 force index(dm_index) where dm = #{dm} or dm = 0")
    ArrayList<String> getVectorFromDm(@Param("dm") int dm);

    @Select("select num from ask_question_110 force index(dm_index) where dm=#{dm} or dm=0")
    ArrayList<Integer> getNumFromDm(@Param("dm") int dm );

    @Select("select vector from ask_question_110 where num=#{num}")
    String getVecFromNum(@Param("num") int num);


}
