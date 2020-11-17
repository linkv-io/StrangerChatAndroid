package com.linkv.strangechatdemo.utils;

/**
 * Created by Xiaohong on 2020/11/17.
 * desc:
 */
public class TimeUtils {

    // 秒数显示。
    public static String transferTimeFormat(Integer seconds) {
        int day=seconds/(60*60*24);//换成天
        int hour=(seconds-(60*60*24*day))/3600;//总秒数-换算成天的秒数=剩余的秒数    剩余的秒数换算为小时
        int minute=(seconds-60*60*24*day-3600*hour)/60;//总秒数-换算成天的秒数-换算成小时的秒数=剩余的秒数    剩余的秒数换算为分
        int second=seconds-60*60*24*day-3600*hour-60*minute;//总秒数-换算成天的秒数-换算成小时的秒数-换算为分的秒数=剩余的秒数
        //System.out.println(day+"天"+hour+"时"+minute+"分"+second+"秒");
        if(hour!=0) {
            return (hour>=10?hour:"0"+hour)+":"+(minute>=10?minute:"0"+minute)+":"+(second>=10?second:"0"+second);
        }else if(minute!=0) {
            return (minute>=10?minute:"0"+minute)+":"+(second>=10?second:"0"+second);
        }else {
            return "00:"+(second>=10?second:"0"+second);
        }
    }
}
