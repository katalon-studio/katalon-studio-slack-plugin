package com.katalon.plugin.slack;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.google.gson.stream.JsonReader;

public class SlackUtil {
    public static final String RES_IS_OK = "isOk";

    public static final String RES_ERROR_MSG = "errorMsg";

    /**
     * Send message to Slack for Team Collaboration
     * 
     * @param msg String message to send
     * @throws SlackException
     */
    public static void sendMessage(String token, String channel, String msg) throws SlackException {
        try {
            Map<String, Object> response = getResponseFromSendingMsg(buildSlackUri(token, channel, msg));

            boolean isOk = (boolean) response.get(RES_IS_OK);
            if (!isOk) {
                String errorMsg = (String) response.get(RES_ERROR_MSG);
                throw new SlackException(errorMsg);
            }
        } catch (IOException | URISyntaxException e) {
            throw new SlackException(e);
        }
    }

    /**
     * Slack URI builder which will encode user info and message into URI
     * 
     * @param token the authentication token
     * @param channel Channel name
     * @param username Bot name
     * @param asIssuedTokenUser if true, Bot name will be ignore. Otherwise, message will be sent using Bot name
     * @param msg Message to send
     * @return URI
     * @throws URISyntaxException
     * @throws Exception UnsupportedEncodingException
     * @see UnsupportedEncodingException
     * @see <a href="https://api.slack.com/web">Create Slack auth token</a>
     */
    public static URI buildSlackUri(String token, String channel, String msg) throws URISyntaxException {
        // scheme:[//[user:password@]host[:port]][/]path[?query][#fragment]
        URIBuilder uriBuilder = new URIBuilder().setScheme("https")
                .setHost("slack.com")
                .setPath("/api/chat.postMessage")
                .addParameter("token", token)
                .addParameter("channel", channel);
        uriBuilder.addParameter("text", msg);
        return uriBuilder.build();
    }

    /**
     * Get response data from sending message to Slack
     * 
     * @param uri URI to send GET request
     * @return A Map object with 2 keys SlackUtil.RES_IS_OK and SlackUtil.RES_ERROR_MSG
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static Map<String, Object> getResponseFromSendingMsg(URI uri) throws ClientProtocolException, IOException {
        boolean isOk = false;
        String errorMsg = null;

        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpget = new HttpGet(uri);
        CloseableHttpResponse response = httpclient.execute(httpget);
        try {
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStreamReader in = new InputStreamReader(entity.getContent());
                try {
                    JsonReader reader = new JsonReader(in);
                    reader.beginObject();
                    while (reader.hasNext()) {
                        String name = reader.nextName();
                        if (StringUtils.equals(name, "ok")) {
                            isOk = reader.nextBoolean();
                        } else if (StringUtils.equals(name, "error")) {
                            errorMsg = reader.nextString();
                        } else {
                            reader.skipValue(); // avoid some unhandled events
                        }
                    }
                    reader.endObject();
                    reader.close();
                } finally {
                    in.close();
                }
            }
        } finally {
            response.close();
        }

        Map<String, Object> res = new HashMap<String, Object>();
        res.put(RES_IS_OK, isOk);
        res.put(RES_ERROR_MSG, errorMsg);
        return res;
    }

    /**
     * Slack Bold format text message
     * 
     * @param msg String text message
     * @return String Bold format text message
     */
    public String fmtBold(String msg) {
        return "*" + msg + "*";
    }

    /**
     * Slack Italic format text message
     * 
     * @param msg String text message
     * @return String Italic format text message
     */
    public String fmtItalic(String msg) {
        return "_" + msg + "_";
    }

    /**
     * Slack responded message status
     */
    public static class SlackMsgStatus {
        private static SlackMsgStatus _instance;

        private static Map<String, String> msgMap;

        public static SlackMsgStatus getInstance() {
            if (_instance == null) {
                _instance = new SlackMsgStatus();
            }
            return _instance;
        }

        public SlackMsgStatus() {
            msgMap = new HashMap<String, String>();
            msgMap.put("channel_not_found", StringConstants.SLACK_ERROR_MSG_CHANNEL_NOT_FOUND);
            msgMap.put("not_in_channel", StringConstants.SLACK_ERROR_MSG_NOT_IN_CHANNEL);
            msgMap.put("is_archived", StringConstants.SLACK_ERROR_MSG_IS_ARCHIVED);
            msgMap.put("msg_too_long", StringConstants.SLACK_ERROR_MSG_MSG_TOO_LONG);
            msgMap.put("no_text", StringConstants.SLACK_ERROR_MSG_NO_TEXT);
            msgMap.put("rate_limited", StringConstants.SLACK_ERROR_MSG_RATE_LIMITED);
            msgMap.put("not_authed", StringConstants.SLACK_ERROR_MSG_NOT_AUTHED);
            msgMap.put("invalid_auth", StringConstants.SLACK_ERROR_MSG_INVALID_AUTH);
            msgMap.put("account_inactive", StringConstants.SLACK_ERROR_MSG_ACCOUNT_INACTIVE);
        }

        public Map<String, String> getMsgMap() {
            return msgMap;
        }

        public String getMsgDescription(String msgCode) {
            if (getMsgMap().get(msgCode) == null) {
                // No message description found
                return msgCode;
            }
            return getMsgMap().get(msgCode);
        }
    }
}
