package com.rockq.icsclient;

import com.rockq.icsclient.messageHandler.MessageDisposeDao;
import com.rockq.icsclient.utils.CharsetUtils;
import com.rockq.icsclient.utils.ListForReceiveMessage;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void addReceiver() {
        byte[] messages = {111, 0, 0, 0, 1, 0, 0, 0, 26, 0, 0, 0, 23, 106, 0, 0, 52, 50, 0, 0, 1, 10, 0, 0, -106, 100, 11, -124, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 35, 35, 35, 35, 53, 2, -50, -62, -54, -86, -74, -56, -76, -85, -72, -48, -58, -9, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 2, 15, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        ListForReceiveMessage.getInstance().addReceiveMessage("192.168.3.145", messages);
        MessageDisposeDao messageDisposeDao = new MessageDisposeDao();
        for (int i = 0; i < ListForReceiveMessage.getInstance().getListSize(); i++) {
            messageDisposeDao.disposeMessage((String) ListForReceiveMessage.getInstance().getReceiveMessage(), (String) ListForReceiveMessage.getInstance().getReceiveMessage());
        }
    }

    @Test
    public void textHex2Stirng() {
        String caaab6c8b4abb8d0c6f7 = CharsetUtils.hexCode2Charset("caaab6c8b4abb8d0c6f7");
        System.out.println(caaab6c8b4abb8d0c6f7);

    }

}