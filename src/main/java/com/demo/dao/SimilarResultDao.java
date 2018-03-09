package com.demo.dao;


import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.ArrayList;


public interface SimilarResultDao {
    @Select("select * from ask_question_110 where num > 105260  ")
    ArrayList<QAEntity> getQAEntity();

    @Select("select bestResponse from ask_question_110 where num = #{num}")
    String getAnswer(int num);

    @Update("update ask_question_110 set dm = #{dm} where num = #{num}")
    void setDm(@Param("num") int num,@Param("dm") int dm);

    @Update("update ask_question_110 set vector = #{vector} where num =#{num}")
    void setVector(@Param("num") int num,@Param("vector") String vector);

}
