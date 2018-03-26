package com.demo;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class test {
    public static void main(String[] args) throws IOException {
        String m = "最近我们燕山油化工股份有限公司运行保障中心想收回每个人95年的劳动合同进行修改，大家都没有对此事有疑义。我想请教一下律师，他们可以修改我们95年的劳动合同吗？谢谢 ";
        String[] ms = m.split("##");
        List<String> players = Arrays.asList(ms);
        players.forEach((player)-> System.out.println(player));


    }
}
