package com.hry.util;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class FramUtil {

    static int x,y;

    /**
     * 给参数中的窗体添加能被鼠标拖拽的功能
     *
     */
    public static void moveFrame(JFrame frame) {
        frame.addMouseListener(new MouseListener() {
            @Override
            public void mouseReleased(MouseEvent e) {
                // TODO Auto-generated method stub

            }
            @Override
            public void mousePressed(MouseEvent e) {
                // 记录按下瞬间状态
                x = e.getX();
                y = e.getY();

            }
            @Override
            public void mouseExited(MouseEvent e) {
                // TODO Auto-generated method stub

            }
            @Override
            public void mouseEntered(MouseEvent e) {
                // TODO Auto-generated method stub

            }
            @Override
            public void mouseClicked(MouseEvent e) {
                // TODO Auto-generated method stub

            }
        });
        // 监听鼠标移动状态
        frame.addMouseMotionListener(new MouseMotionListener() {

            @Override
            public void mouseMoved(MouseEvent e) {
                // TODO Auto-generated method stub

            }

            /**
             * 鼠标在按下状态的移动时，触发下面的方法
             */
            @Override
            public void mouseDragged(MouseEvent e) {
                // 拖拽发生时，当前的窗体位置
                int frame_x = frame.getX();
                int frame_y = frame.getY();
                // 获取鼠标的偏移量
                int x1 = e.getX();
                int y1 = e.getY();
                // 重新设置窗体的位置，初始位置+偏移量
                frame.setLocation(frame_x + (x1 - x), frame_y + (y1 - y));
            }
        });
    }

}
