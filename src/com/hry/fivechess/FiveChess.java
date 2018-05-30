package com.hry.fivechess;

import com.hry.util.FramUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FiveChess extends MouseAdapter implements ActionListener {
    // 应用窗口，显示在屏幕上
    private JFrame frame;
    // 标签，在窗口中显示，显示主背景
    private JLabel backL;
    // 画布
    private JPanel canvas;
    // 菜单条
    private JMenuBar menuBar;
    // 设置显示图片
    private JMenu menu;
    private JMenuItem save, load;

    // 背景图片
    ImageIcon backImg = new ImageIcon("images/back.png");
    // 图标
    ImageIcon barImg = new ImageIcon("images/bar.png");
    ImageIcon blackUserImg = new ImageIcon("images/blackmsg.png");
    ImageIcon huiqiImg = new ImageIcon("images/huiqi.png");
    ImageIcon minImg = new ImageIcon("images/min.png");
    ImageIcon closeImg = new ImageIcon("images/close.png");
    ImageIcon startImg = new ImageIcon("images/start.png");
    ImageIcon whiteUserImg = new ImageIcon("images/whitemsg.png");
    ImageIcon iconImg = new ImageIcon("images/icon.jpg");
    Image panelImg = new ImageIcon("images/panel.png").getImage();
    Image white = new ImageIcon("images/white.png").getImage();
    Image black = new ImageIcon("images/black.png").getImage();
    // 标签上添加的元素
    JLabel user1, user2;
    JButton minBtn, closeBtn, lastStep, startBtn;
    // 记录当前鼠标点击位置， 转换成在棋盘上的坐标
    int row = -10, col;
    // 记录黑白棋
    boolean isBlack;
    // 记录棋盘上棋子的所有位置 0 表示没有 1 表示黑 2 表示白
    int[][] chesses = new int[MyPanel.ROWS][MyPanel.COLS];
    // 记录落子步骤的List集合
    List<String> steps = new ArrayList<>();

    /**
     * 构造器
     */
    public FiveChess() {
        frame = new JFrame();
        isBlack = true;
        // 添加背景标签
        backL = new JLabel(backImg);
        frame.setIconImage(iconImg.getImage());
        init();
        // 添加事件管理
        addEventHandler();
        frame.add(backL);
        // 去除边框
        frame.setUndecorated(true);
        // 自适应大小
        frame.pack();
        // 设置主显示器居中
        frame.setLocationRelativeTo(null);
        // 设置窗体可以随着鼠标拖拽而移动
        FramUtil.moveFrame(frame);
        // 设置可见
        frame.setVisible(true);
    }

    /**
     * 设置监听器
     */
    private void addEventHandler() {
        minBtn.addActionListener(this);
        closeBtn.addActionListener(this);
        lastStep.addActionListener(this);
        startBtn.addActionListener(this);
        canvas.addMouseListener(this);
        save.addActionListener(this);
        load.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == minBtn) {
            // 缩放按钮监听功能
            frame.setState(JFrame.ICONIFIED);
        } else if (e.getSource() == closeBtn) {
            System.exit(0);
        } else if (e.getSource() == lastStep) {
            lastStep();
        } else if (e.getSource() == startBtn) {
            start();
        } else if (e.getSource() == save) {
            save();
        } else if (e.getSource() == load) {
            load();
        }

    }

    /**
     * 对点击事件监听
     *
     * @param e
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        super.mouseClicked(e);
        // 获取鼠标点击的位置转换成矩阵坐标
        int x = e.getX();
        int y = e.getY();
        // （ 鼠标的真实坐标 - 棋盘偏移量 ） / 格子间距 = a.bcd 个格子
        row = Math.round((float) (y - MyPanel.UP) / MyPanel.SPACE);
        col = Math.round((float) (x - MyPanel.LEFT) / MyPanel.SPACE);
        if (row < 0 || col < 0 || row >= MyPanel.ROWS || col >= MyPanel.COLS || chesses[row][col] != 0) {
            JOptionPane.showMessageDialog(frame, "请在其它地方落子");
            return;
        }
        // 判断是黑棋还是白棋
        if (isBlack) {
            // 黑
            chesses[row][col] = 1;
        } else {
            // 白
            chesses[row][col] = 2;
        }

        // 落子信息存入集合
        steps.add(row + ":" + col + ":" + (isBlack ? 1 : 2));
        // 重绘
        canvas.repaint();

        if (win(row, col, isBlack ? 1 : 2)) {
            String msg = isBlack ? "黑棋胜" : "白棋胜 Yes:再来一句; No:退出游戏";
            int select = JOptionPane.showConfirmDialog(frame, msg, "消息框", JOptionPane.YES_NO_OPTION);
            if (select == JOptionPane.YES_OPTION) {
                start();
                return;
            } else if (select == JOptionPane.NO_OPTION) {
                System.exit(0);
            }
        }
        // 反转
        isBlack = !isBlack;

    }

    /**
     * 初始化 为背景标签添加东西
     */
    private void init() {
        // 添加用户图片
        user1 = new JLabel(blackUserImg);
        user1.setBounds(28, 100, 110, 110);
        backL.add(user1);
        user2 = new JLabel(whiteUserImg);
        user2.setBounds(764, 520, 110, 110);
        backL.add(user2);

        // 添加最小化按钮
        minBtn = new JButton(minImg);
        minBtn.setBorderPainted(false);
        minBtn.setContentAreaFilled(false);
        minBtn.setBounds(840, 20, 12, 14);
        backL.add(minBtn);

        // 添加关闭按钮
        closeBtn = new JButton(closeImg);
        closeBtn.setBorderPainted(false);
        closeBtn.setContentAreaFilled(false);
        closeBtn.setBounds(870, 20, 12, 14);
        backL.add(closeBtn);

        // 添加刷新按钮
        startBtn = new JButton(startImg);
        startBtn.setBorderPainted(false);
        startBtn.setContentAreaFilled(false);
        startBtn.setBounds(450, 10, 34, 34);
        backL.add(startBtn);

        // 添加悔棋按钮
        lastStep = new JButton(huiqiImg);
        lastStep.setBorderPainted(false);
        lastStep.setContentAreaFilled(false);
        lastStep.setBounds(36, 450, 100, 42);
        backL.add(lastStep);

        //
        menu = new JMenu("loding");
        menu.setIcon(barImg);
        menu.setBorderPainted(false);

        save = new JMenuItem("保存游戏");
        load = new JMenuItem("读取存档");
        Font font = new Font("微软雅黑", Font.BOLD, 22);
        save.setFont(font);
        load.setFont(font);
        Color color = new Color(255, 0, 0);
        save.setForeground(color);
        load.setForeground(color);
        menu.add(save);
        menu.add(load);

        menuBar = new JMenuBar();
        menuBar.setOpaque(false);
        menuBar.setBorderPainted(false);
        menuBar.add(menu);
        menuBar.setBounds(10, 5, 110, 36);
        backL.add(menuBar);

        // 创建panel对象
        canvas = new MyPanel();
        canvas.setOpaque(false);
        canvas.setBounds(168, 85, 592, 592);
        backL.add(canvas);

    }

    /**
     * 继承JPanel重写 使用内部类 方便在内部使用外部类的资源
     */
    private class MyPanel extends JPanel {
        static final int SPACE = 30;
        static final int ROWS = 18;
        static final int COLS = 18;
        // 偏移量
        static final int LEFT = 30, UP = 30;
        static final int CHESS_R = 13;

        public void paint(Graphics g) {
            // 传递画笔给父类方法
            super.paint(g);
            g.drawImage(panelImg, 0, 0, null);
            g.translate(LEFT, UP);
            // 绘制横向线
            for (int i = 0; i < ROWS; i++) {
                g.drawLine(0, 0 + i * SPACE, (COLS - 1) * SPACE, 0 + i * SPACE);
            }
            // 绘制纵向线
            for (int i = 0; i < COLS; i++) {
                g.drawLine(i * SPACE, 0, i * SPACE, SPACE * (ROWS - 1));
            }
            // 绘制棋子
            // g.drawImage(black, SPACE * col - ChessR, SPACE * row - ChessR,
            // null);
            for (int i = 0; i < chesses.length; i++) {
                for (int j = 0; j < chesses.length; j++) {
                    if (chesses[i][j] == 1) {
                        g.drawImage(black, SPACE * j - CHESS_R, SPACE * i - CHESS_R, null);
                    } else if (chesses[i][j] == 2) {
                        g.drawImage(white, SPACE * j - CHESS_R, SPACE * i - CHESS_R, null);
                    }
                }
            }
            // 绘制包围棋子的正方型
            if (col != -10) {
                g.setColor(Color.RED);
                g.drawRect(col * SPACE - CHESS_R, row * SPACE - CHESS_R, CHESS_R * 2, CHESS_R * 2);
            }
        }

    }

    /**
     * 判断赢，横，左倾斜，右倾斜，是否有连续相同的五个颜色棋子
     */
    public boolean win(int x, int y, int color) {
        // 记录连续棋子的数目
        int count = 0;
        // 横向判断是不是连续五个数字
        for (int i = y - 4; i < y + 5; i++) {
            // 保证不出界
            if (i >= 0 && i < MyPanel.COLS) {
                if (chesses[x][i] == color) {
                    count++;
                    if (count == 5) {
                        return true;
                    }
                } else {
                    count = 0;
                }
            }
        }

        // 纵向
        count = 0;
        for (int i = x - 4; i < x + 5; i++) {
            if (i >= 0 && i < MyPanel.ROWS) {
                if (chesses[i][y] == color) {
                    count++;
                    if (count == 5) {
                        return true;
                    }
                } else {
                    count = 0;
                }
            }
        }

        // 右上
        count = 0;
        for (int i = x + 4, j = y - 4; i >= x - 4 && j <= y + 4; i--, j++) {
            if (i >= 0 && j >= 0 && i < MyPanel.ROWS && j < MyPanel.COLS) {
                if (chesses[i][j] == color) {
                    count++;
                    if (count == 5) {
                        return true;
                    }
                } else {
                    count = 0;
                }
            }
        }

        // 左上
        count = 0;
        for (int i = x - 4, j = y - 4; i <= x + 4 && j <= y + 4; i++, j++) {
            if (i >= 0 && j >= 0 && i < MyPanel.ROWS && j < MyPanel.COLS) {
                if (chesses[i][j] == color) {
                    count++;
                    if (count == 5) {
                        return true;
                    }
                } else {
                    count = 0;
                }
            }
        }

        return false;
    }

    /**
     * 重新开始的方法
     */
    private void start() {
        // 创建一个新的数组
        chesses = new int[MyPanel.ROWS][MyPanel.COLS];
        row = -10;
        isBlack = true;
        // 重绘界面
        // 集合清空
        steps.clear();
        canvas.repaint();
    }

    /**
     * 悔棋步骤
     */
    private void lastStep() {
        if (steps.size() <= 0) {
            JOptionPane.showConfirmDialog(frame, "无路可退！", "提示", JOptionPane.CANCEL_OPTION);
            return;
        }
        // 从steps中获取并移除最后一个元素
        String str = steps.remove(steps.size() - 1);
        // 落子颜色也要调整
        isBlack = !isBlack;
        // 从str中获取位置信息
        String[] ss = str.split(":");
        // 在chesses中删除（x,y）中的信息 （chesses[x][y] = 0)
        int x = Integer.parseInt(ss[0]);
        int y = Integer.parseInt(ss[1]);
        chesses[x][y] = 0;
        if (steps.size() == 0) {
            row = -10;
            canvas.repaint();

        } else {
            // steps中获取当前的最后一个元素s 存放方框
            str = steps.get(steps.size() - 1);
            ss = str.split(":");
            row = Integer.parseInt(ss[0]);
            col = Integer.parseInt(ss[1]);
            canvas.repaint();
        }
    }

    /**
     * 保存游戏功能
     */
    private void save() {
        // 文件选择器
        JFileChooser jfc = new JFileChooser();
        // 弹出保存对话框
        jfc.showSaveDialog(frame);
        // 想要存数据的文件
        File file = jfc.getSelectedFile();
        // 把steps文件逐个存入硬盘file位置表示的文件上
        PrintWriter out = null;
        try {
            // out能想硬盘上传递字符串
            out = new PrintWriter(file);
            for (String s : steps) {
                // 每传送一个 s 后跟一个换行
                out.println(s);
            }
            out.flush();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            out.close();
        }
    }

    /**
     * 加载游戏
     */
    private void load() {
        JFileChooser jfc = new JFileChooser();
        jfc.showOpenDialog(frame);
        File file = jfc.getSelectedFile();
        if (file == null) {
            return;
        }
        BufferedReader in = null;
        String[] s;
        int x = 0, y = 0, color = 0;
        try {
            // 先把步骤集合清空
            steps.clear();
            // 存棋子位置的数组也清空
            chesses = new int[MyPanel.ROWS][MyPanel.COLS];
            in = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            // 把里面的步骤依次存入steps集合（并存入数组）
            String step;
            while ((step = in.readLine()) != null) {
                // 解析step变成三个整数
                s = step.split(":");
                // step存入步骤集合
                steps.add(step);
                x = Integer.parseInt(s[0]);
                y = Integer.parseInt(s[1]);
                color = Integer.parseInt(s[2]);
                chesses[x][y] = color;
            }
            // 保存最后一个步骤的位置 用来绘制标记方框
            row = x;
            col = y;
            isBlack = color == 1 ? false : true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            canvas.repaint();
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

    }

}
