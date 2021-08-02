package com.learn.thread.basics.chapter_four;

public class TicketWindow extends Thread{

    private final String name;

    public TicketWindow(String name) {
        this.name = name;
    }

    private static final int MAX = 50;

    private static int index = 1;

    @Override
    public void run() {
        while (index <= MAX) {
            System.out.println(" 柜台：" + name + " 当前的号码是：" + (index++));
        }
    }

    public static void main(String[] args) {
        TicketWindow ticketWindowl = new TicketWindow("一号出号机");
        ticketWindowl. start () ;
        TicketWindow ticketWindow2 = new TicketWindow("二号出号机");
        ticketWindow2.start();
        TicketWindow ticketWindow3 = new TicketWindow("三号出号机");
        ticketWindow3. start () ;
        TicketWindow ticketWindow4 = new TicketWindow("四号出号机");
        ticketWindow4.start();
    }
}
