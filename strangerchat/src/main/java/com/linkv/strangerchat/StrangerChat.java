package com.linkv.strangerchat;

import android.app.Application;
import android.text.TextUtils;
import android.view.ViewGroup;

import com.im.imcore.IMBridger;
import com.im.imlogic.IMMsg;
import com.im.imlogic.LVIMSDK;
import com.linkv.lvrtmsdk.LVRTMEngine;
import com.linkv.rtcsdk.LinkVRTCEngine;
import com.linkv.rtcsdk.bean.VideoQuality;
import com.linkv.strangerchat.utils.JsonUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Xiaohong on 2020/11/2.
 * desc:
 */
public class StrangerChat extends LVRTMEngine implements IMBridger.IMModuleEventListener, IMBridger.IMReceiveMessageListener, LVRTMEngine.IRoomEventHandler {
    static final String LINKV_VIDEO_CALL = "linkv_video_call";
    static final String LINKV_ANWSER_CALL = "linkv_anwser_call";
    static final String LINKV_HANGUP_CALL = "linkv_hangup_call";
    static final String LINKV_GIFT = "linkv_gift";
    static final String LINKV_APPLY_FOR_BEAM = "linkv_apply_for_beam";
    static final String LINKV_ANWSER_APPLY_FOR_BEAM = "linkv_anwser_apply_for_beam";
    static final String LINKV_KICK_BEAM_OFF = "linkv_kick_beam_off";
    static final String LINKV_END_ROOM = "linkv_end_room";
    static final String LINKV_ENTER_ROOM = "linkv_enter_room";
    static final String LINKV_LEAVE_ROOM = "linkv_leave_room";
    static final String LINKV_ENABLE_MIC = "linkv_enable_mic";     // 开启或关闭麦克风
    static final String LINKV_ENABLE_VIDEO = "linkv_enable_video"; // 开启或关闭摄像头
    private static final String KEY_ISAUDIO = "isAudio";
    private static final String KEY_EXTRA = "extra";


    IMEventHandler mIMEventHandler;
    StrangerRoomHandler mStrangerRoomHandler;
    private String mRoomId;

    public static StrangerChat createEngine(Application application, String appId, String appKey, boolean isDebug, LinkVRTCEngine.IInitHandler iInitHandler, IMEventHandler imEventHandler) {
        StrangerChat strangerChat = new StrangerChat(application, appId, appKey, appId, appKey, SDK_TYPE_LVRTC, isDebug, true, iInitHandler, imEventHandler);
        return strangerChat;
    }

    public StrangerChat(Application application, String rtcAppID, String rtcAppKey, String imAppID, String imAppKey, int sdkType, boolean isDebug, boolean isInternationalEnv, LinkVRTCEngine.IInitHandler iInitHandler, IMEventHandler imEventHandler) {
        super(application, rtcAppID, rtcAppKey, imAppID, imAppKey, sdkType, isDebug, isInternationalEnv, iInitHandler);
        mIMEventHandler = imEventHandler;
//        setRoomEventHandler(this);
        setGlobalReceiveMessageListener(this);
        setIMAuthEventListener(this);
    }

    /*----  陌生人聊天增加的方法 ------*/

    // 呼叫指定用户
    public int call(String uid, boolean isAudio, String extra, IMBridger.IMSendMessageListener listener) {

        if (TextUtils.isEmpty(uid)) {
            return -1;
        }

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(KEY_ISAUDIO, isAudio);
            jsonObject.put(KEY_EXTRA, extra);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String content = jsonObject.toString();
        IMMsg msg = IMMsg.buildEventMessage(uid, LINKV_VIDEO_CALL, content, null, null, null, null, null);
        if (msg == null) {
            return -2;
        }
        return LVIMSDK.sharedInstance().sendMessage(msg, null, listener);
    }

    /**
     * 同意或拒绝某个用户的呼叫
      * @param tid 对方Id
     * @param accept true为接听，false为挂断。
     * @param isAudio 是否仅语音
     * @param extra 附加数据
     * @param listener 结果回调
     * @return 0为正常，非0为错误码
     */
    public int answerCall(String tid, boolean accept, boolean isAudio, String extra, IMBridger.IMSendMessageListener listener) {

        if (TextUtils.isEmpty(tid)) {
            return -1;
        }
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("accept", accept);
            jsonObject.put("isAudio", isAudio);
            jsonObject.put("extra", extra);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String content = jsonObject.toString();
        IMMsg msg = IMMsg.buildEventMessage(tid, LINKV_ANWSER_CALL, content, null, null, null, null, null);
        if (msg == null) {
            return -2;
        }
        return LVIMSDK.sharedInstance().sendMessage(msg, null, listener);
    }

    // 主动挂断电话
    public int hangupCall(String uid, String extra, IMBridger.IMSendMessageListener listener) {
        if (TextUtils.isEmpty(uid)) {
            return -1;
        }
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("extra", extra);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String content = jsonObject.toString();
        IMMsg msg = IMMsg.buildEventMessage(uid, LINKV_HANGUP_CALL, content, null, null, null, null, null);
        if (msg == null) {
            return -2;
        }
        return LVIMSDK.sharedInstance().sendMessage(msg, null, listener);
    }

    //public void setRoomDelegate


    public void startCapture(String userId, ViewGroup container, boolean isZOrderMediaOverlay) {
        startPreview(userId, container, isZOrderMediaOverlay);
        sendRoomMessage(mRoomId, "1", LINKV_ENABLE_VIDEO, null);
    }

    @Override
    public void stopCapture() {
        super.stopPreview();
        sendRoomMessage(mRoomId, "0", LINKV_ENABLE_VIDEO, null);
    }

    // 发送礼物
    public int sendGift(String giftId, int count, List<String> uids, String roomId, String extra, IMBridger.IMSendMessageListener listener) {
        if (TextUtils.isEmpty(giftId) || TextUtils.isEmpty(roomId)) {
            return -1;
        }
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("extra", extra);
            jsonObject.put("uids", JsonUtil.listToJson(uids));
            jsonObject.put("giftId", giftId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String content = jsonObject.toString();
        IMMsg msg = IMMsg.buildChatRoomMessage(roomId, LINKV_GIFT, content);
        if (msg == null) {
            return -2;
        }
        return LVIMSDK.sharedInstance().sendMessage(msg, null, listener);
    }

    // 申请上麦
    public int applyForBeam(String roomId, int position, String extra, IMBridger.IMSendMessageListener listener) {
        if (TextUtils.isEmpty(roomId)) {
            return -1;
        }
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("extra", extra);
            jsonObject.put("position", position);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String content = jsonObject.toString();
        IMMsg msg = IMMsg.buildChatRoomMessage(roomId, LINKV_APPLY_FOR_BEAM, content);
        if (msg == null) {
            return -2;
        }
        return LVIMSDK.sharedInstance().sendMessage(msg, null, listener);
    }

    // 同意或拒绝某个用户的上麦
    public int answerApplyForBeam(String uid, boolean accept, String roomId, int position, String extra, IMBridger.IMSendMessageListener listener) {
//        if (!roomId || !uid) return -1;
//        NSMutableDictionary *dictM = [NSMutableDictionary dictionary];
//        dictM[@"accept"] = @(accept);
//        dictM[@"position"] = @(position);
//        dictM[@"extra"] = extra;
//        dictM[@"uid"] = uid;
//        NSString *content = [self dictToStr:dictM];
//        LVIMMessage *message = [self sendRoomMessage:roomId content:content msgType:linkv_anwser_apply_for_beam complete:callback];
//        return message == nil ? -1 : 0;

        if (TextUtils.isEmpty(roomId) || TextUtils.isEmpty(uid)) {
            return -1;
        }
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("extra", extra);
            jsonObject.put("position", position);
            jsonObject.put("accept", accept);
            jsonObject.put("uid", uid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String content = jsonObject.toString();
        IMMsg msg = IMMsg.buildChatRoomMessage(roomId, LINKV_ANWSER_APPLY_FOR_BEAM, content);
        if (msg == null) {
            return -2;
        }
        return LVIMSDK.sharedInstance().sendMessage(msg, null, listener);
    }

    // 让某个用户下麦
    public int kickBeamOff(String uid, String roomId, int position, String extra, IMBridger.IMSendMessageListener listener) {
        if (TextUtils.isEmpty(roomId) || TextUtils.isEmpty(uid)) {
            return -1;
        }
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("extra", extra);
            jsonObject.put("position", position);
            jsonObject.put("uid", uid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String content = jsonObject.toString();
        IMMsg msg = IMMsg.buildChatRoomMessage(roomId, LINKV_KICK_BEAM_OFF, content);
        if (msg == null) {
            return -2;
        }
        return LVIMSDK.sharedInstance().sendMessage(msg, null, listener);
    }

    // 关闭房间
    public int endRoom(String roomId, String extra, IMBridger.IMSendMessageListener listener) {
        if (TextUtils.isEmpty(roomId)) {
            return -1;
        }
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("extra", extra);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String content = jsonObject.toString();
        IMMsg msg = IMMsg.buildChatRoomMessage(roomId, LINKV_END_ROOM, content);
        if (msg == null) {
            return -2;
        }
        return LVIMSDK.sharedInstance().sendMessage(msg, null, listener);
    }


    @Override
    public void onQueryIMToken() {
        if (mIMEventHandler != null) {
            mIMEventHandler.onQueryIMToken();
        }
    }

    @Override
    public void onIMAuthFailed(String uid, String token, int ecode, int rcode, boolean isTokenExpired) {
        if (mIMEventHandler != null) {
            mIMEventHandler.onIMAuthFailed(uid, token, ecode, rcode, isTokenExpired);
        }
    }

    @Override
    public void onIMAuthSucceed(String uid, String token, long unReadMsgSize) {
        if (mIMEventHandler != null) {
            mIMEventHandler.onIMAuthSucceed(uid, token, unReadMsgSize);
        }
    }

    @Override
    public void onIMTokenExpired(String uid, String token) {
        if (mIMEventHandler != null) {
            mIMEventHandler.onIMTokenExpired(uid, token);
        }
    }

    @Override
    public boolean onIMReceiveMessageFilter(int cmdtype, int subtype, int diytype, int dataid, String fromid, String toid, String msgType, byte[] msgContent, int waitings, int packetSize, int waitLength, int bufferSize) {
        return false;
    }

    @Override
    public int onIMReceiveMessageHandler(String owner, IMMsg msg, int waitings, int packetSize, int waitLength, int bufferSize) {
        if (msg == null || mIMEventHandler == null) {
            return 0;
        }

        int result = 0;
        String msgType = msg.getMsgType();
        if (msg.isChatroomMessage()) {


        } else {
            if (msg.isEventMessage()) {
                if (LINKV_VIDEO_CALL.equals(msgType)) {
                    try {
                        JSONObject jsonObject = new JSONObject(msg.getMsgContent());
                        boolean isAudio = jsonObject.optBoolean("isAudio");
                        String extra = jsonObject.optString("extra");
                        result = mIMEventHandler.onCallReceived(msg.fromID, isAudio, msg.stime, extra);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (LINKV_ANWSER_CALL.equals(msgType)) {
                    try {
                        JSONObject jsonObject = new JSONObject(msg.getMsgContent());
                        boolean accept = jsonObject.optBoolean("accept");
                        boolean isAudio = jsonObject.optBoolean("isAudio");
                        String extra = jsonObject.optString("extra");
                        result = mIMEventHandler.onAnswerCallReceived(msg.fromID, accept, isAudio, msg.stime, extra);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (LINKV_HANGUP_CALL.equals(msgType)) {
                    try {
                        JSONObject jsonObject = new JSONObject(msg.getMsgContent());
                        String extra = jsonObject.optString("extra");
                        result = mIMEventHandler.onHangupCallReceived(msg.fromID, extra);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    result = mIMEventHandler.onIMReceiveMessageHandler(owner, msg, waitings, packetSize, waitLength, bufferSize);
                }
            } else {
                result = mIMEventHandler.onIMReceiveMessageHandler(owner, msg, waitings, packetSize, waitLength, bufferSize);
            }
        }

        return result;
    }

    @Override
    public void onRemoteStreamEnd(String userId) {
        if (mStrangerRoomHandler != null) {
            mStrangerRoomHandler.onRemoteStreamEnd(userId);
        }
    }

    @Override
    public void onRemoteStreamAdd(String userId) {
        if (mStrangerRoomHandler != null) {
            mStrangerRoomHandler.onRemoteStreamAdd(userId);
        }
    }

    @Override
    public void onRoomDisconnect(int errorCode, String roomID) {
        if (mStrangerRoomHandler != null) {
            mStrangerRoomHandler.onRoomDisconnect(errorCode, roomID);
        }
    }


    @Override
    public void onRoomConnected(String roomID) {
        mRoomId = roomID;
        if (mStrangerRoomHandler != null) {
            mStrangerRoomHandler.onRoomConnected(roomID);
        }
    }

    @Override
    public void onRemoteQualityUpdate(String streamID, VideoQuality quality) {
        if (mStrangerRoomHandler != null) {
            mStrangerRoomHandler.onRemoteQualityUpdate(streamID, quality);
        }
    }

    @Override
    public void onPublisherQualityUpdate(String streamID, VideoQuality quality) {
        if (mStrangerRoomHandler != null) {
            mStrangerRoomHandler.onPublisherQualityUpdate(streamID, quality);
        }
    }

    @Override
    public void onVideoSizeChanged(String streamID, int width, int height) {
        if (mStrangerRoomHandler != null) {
            mStrangerRoomHandler.onVideoSizeChanged(streamID, width, height);
        }
    }

    @Override
    public void onRoomMessageReceive(IMMsg msg) {
        if (mStrangerRoomHandler != null) {
            String msgType = msg.getMsgType();
            if (LINKV_GIFT.equals(msgType)) {
                try {
                    JSONObject jsonObject = new JSONObject(msg.getMsgContent());
                    String giftId = jsonObject.optString("giftId");
                    int count = jsonObject.optInt("count");
                    String extra = jsonObject.optString("extra");
                    String uidsJson = jsonObject.optString("uids");
                    List<String> uidList = JsonUtil.jsonToList(uidsJson);
                    mStrangerRoomHandler.onGiftReceived(giftId, count, msg.fromID, uidList, msg.toID, extra);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (LINKV_APPLY_FOR_BEAM.equals(msgType)) {
                try {
                    JSONObject jsonObject = new JSONObject(msg.getMsgContent());
                    int position = jsonObject.optInt("position");
                    String extra = jsonObject.optString("extra");
                    mStrangerRoomHandler.onApplyForBeamReceived(msg.fromID, msg.toID, position, extra);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (LINKV_ANWSER_APPLY_FOR_BEAM.equals(msgType)) {
                try {
                    JSONObject jsonObject = new JSONObject(msg.getMsgContent());
                    String uid = jsonObject.optString("uid");
                    boolean accept = jsonObject.optBoolean("accept");
                    int position = jsonObject.optInt("position");
                    String extra = jsonObject.optString("extra");
                    mStrangerRoomHandler.onAnswerApplyForBeam(uid, accept, msg.toID, position, extra);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (LINKV_KICK_BEAM_OFF.equals(msgType)) {
                try {
                    JSONObject jsonObject = new JSONObject(msg.getMsgContent());
                    String uid = jsonObject.optString("uid");
                    int position = jsonObject.optInt("position");
                    String extra = jsonObject.optString("extra");
                    mStrangerRoomHandler.onKickBeamOffReceived(uid, msg.toID, position, extra);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (LINKV_END_ROOM.equals(msgType)) {
                try {
                    JSONObject jsonObject = new JSONObject(msg.getMsgContent());
                    String extra = jsonObject.optString("extra");
                    mStrangerRoomHandler.onEndRoomReceived(msg.toID, extra);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (LINKV_ENTER_ROOM.equals(msgType)) {
                mStrangerRoomHandler.onUserEntered(msg.fromID, msg.toID);
            } else if (LINKV_LEAVE_ROOM.equals(msgType)) {
                mStrangerRoomHandler.onUserLeft(msg.fromID, msg.toID);
            } else if (LINKV_ENABLE_MIC.equals(msgType)) {
                boolean open = Boolean.parseBoolean(msg.getMsgContent());
                mStrangerRoomHandler.onUserMicrophoneChanged(msg.fromID, msg.toID, open);
            } else if (LINKV_ENABLE_VIDEO.equals(msgType)) {
                boolean open = "1".equals(msg.getMsgContent());
                mStrangerRoomHandler.onUserVideoCameraChanged(msg.fromID, msg.toID, open);
            } else {
                mStrangerRoomHandler.onRoomMessageReceive(msg);
            }
        }
    }

    @Override
    public void onPublishStateUpdate(int state) {
        if (mStrangerRoomHandler != null) {
            mStrangerRoomHandler.onPublishStateUpdate(state);
        }
    }

    @Override
    public void onPlayStateUpdate(int state, String userId) {
        if (mStrangerRoomHandler != null) {
            mStrangerRoomHandler.onPlayStateUpdate(state, userId);
        }
    }


    public void setStrangerRoomHandler(StrangerRoomHandler strangerRoomHandler) {
        super.setRoomEventHandler(this);
        mStrangerRoomHandler = strangerRoomHandler;
    }


    public interface StrangerRoomHandler extends IRoomEventHandler {
        // 通话时长变化，每秒更新一次，单位(秒)
        void onCallTimeChanged(int duration, String roomId);

        // 收到某个用户发送给一批用户的礼物消息
        int onGiftReceived(String giftId, int count, String sendUid,
                           List<String> uids, String roomId, String extra);

        // 收到某个用户的上麦申请
        int onApplyForBeamReceived(String uid, String roomId, int position, String extra);

        // 收到上麦同意或拒绝的指令
        int onAnswerApplyForBeam(String uid, boolean accept, String roomId, int position, String extra);

        // 收到下麦指令
        int onKickBeamOffReceived(String uid, String roomId, int position, String extra);

        // 收到关闭房间消息
        int onEndRoomReceived(String roomId, String extra);

        // 某个用户进入房间
        int onUserEntered(String uid, String roomId);

        // 某个用户离开房间
        int onUserLeft(String uid, String roomId);

        // 某个用户的麦克风状态发生了变化
        int onUserMicrophoneChanged(String uid, String roomId, boolean isOpen);

        // 某个用户的摄像头状态发生了变化
        int onUserVideoCameraChanged(String uid, String roomId, boolean isOpen);


    }


    public interface IMEventHandler {

        // 收到某个用户的呼叫
        int onCallReceived(String uid, boolean isAudio, long timestamp, String extra);

        // 收到某个用户同意或拒绝的指令
        int onAnswerCallReceived(String uid, boolean accept, boolean isAudio, long timestamp, String extra);

        // 收到某个用户挂断通话的回调
        int onHangupCallReceived(String uid, String extra);


        void onQueryIMToken();

        void onIMAuthFailed(String uid, String token, int ecode, int rcode, boolean isTokenExpired);

        void onIMAuthSucceed(String uid, String token, long unReadMsgSize);

        void onIMTokenExpired(String uid, String token);

        int onIMReceiveMessageHandler(String owner, IMMsg msg, int waitings, int packetSize, int waitLength, int bufferSize);


    }


}
