package com.demo.util;

import com.demo.object.VecCell;

import java.util.Iterator;
import java.util.List;

/**
 * @author d-xsj
 * mail coder_xushijia@126.com
 * date 21/03/2018
 * Description:
 */
public class VecCal {

    public float getMaxClearXY(List<VecCell> list){
        float re = list.get(0).getKey();
        int x = list.get(0).getX();
        int y = list.get(0).getY();
        Iterator it = list.iterator();
        while (it.hasNext()){
            VecCell vecCell = (VecCell) it.next();
            if (vecCell.getX()==x||vecCell.getY()==y){
                it.remove();
            }
        }
        return re;
    }

}
