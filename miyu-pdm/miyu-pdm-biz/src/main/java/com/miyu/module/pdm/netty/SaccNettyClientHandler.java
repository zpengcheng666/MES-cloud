package com.miyu.module.pdm.netty;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import common.bean.SaccNettyBean;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class SaccNettyClientHandler  extends ChannelInboundHandlerAdapter {
    private int byteRead;
    private volatile long start = 0;
    private volatile int lastLength = 0;
    public RandomAccessFile randomAccessFile;
    private SaccNettyBean wnb;


    public SaccNettyClientHandler(SaccNettyBean wnb) {
        if(wnb.getUpOrDownLoadFlag().equals("upload")) {
            if (wnb.getFile().exists()) {
                if (!wnb.getFile().isFile()) {
                    System.out.println("Not a file :" + wnb.getFile());
                    return;
                }
            }
        }
        this.wnb = wnb;
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        ctx.flush();
        ctx.close();
    }

    public void channelActive(ChannelHandlerContext ctx) {
        if(wnb.getUpOrDownLoadFlag().equals("upload")) {
            try {
                randomAccessFile = new RandomAccessFile(wnb.getFile(), "r");
                randomAccessFile.seek(wnb.getStarPos());
                lastLength = 1024 * 10;
                System.out.println("【文件大小】："+randomAccessFile.length());
                if (randomAccessFile.length() <= lastLength) {
                    lastLength = (int) randomAccessFile.length();
                    byte[] bytes = new byte[lastLength];
                    if ((byteRead = randomAccessFile.read(bytes)) != -1) {
                        wnb.setEndPos(byteRead);
                        wnb.setBytes(bytes);
                        ctx.writeAndFlush(wnb);
                        randomAccessFile.close();
                        ctx.close();
                        System.out.println("------【上传完毕！！！】-----");
                    }
                }else{
                    byte[] bytes = new byte[lastLength];
                    if ((byteRead = randomAccessFile.read(bytes)) != -1) {
                        wnb.setEndPos(byteRead);
                        wnb.setBytes(bytes);
                        ctx.writeAndFlush(wnb);
                        randomAccessFile.close();
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException i) {
                i.printStackTrace();
            }
        }else if(wnb.getUpOrDownLoadFlag().equals("download")) {
            ctx.writeAndFlush(wnb);
        }

    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        SaccNettyBean wnb = (SaccNettyBean) msg;
        if (wnb.getUpOrDownLoadFlag().equals("upload")) {
            if (msg instanceof SaccNettyBean) {
                start = (Long) wnb.getStart();
                if (start != -1) {
                    randomAccessFile = new RandomAccessFile(wnb.getFile(), "r");
                    randomAccessFile.seek(start);
                    lastLength = 1024 * 10;
                    long surplusLength = (randomAccessFile.length() - start);
                    if (surplusLength < lastLength) {
                        lastLength = (int) surplusLength;
                    }
                    byte[] bytes = new byte[lastLength];
                    System.out.println("【已上传】:"+(Math.round(start*100/randomAccessFile.length()))+"%");
                    System.out.println("【文件总大小】："+randomAccessFile.length() +"-----"+"【剩余传输大小】:"+surplusLength);
                    if(surplusLength < 1024*10 ){
                        byteRead = randomAccessFile.read(bytes);
                        wnb.setEndPos(byteRead);
                        wnb.setBytes(bytes);
                        ctx.writeAndFlush(wnb);
                        randomAccessFile.close();
                        ctx.close();
                        System.out.println("【已上传】:100%");
                        System.out.println("------【上传完毕！！！】-----");
                    }else{
                        byteRead = randomAccessFile.read(bytes);
                        wnb.setEndPos(byteRead);
                        wnb.setBytes(bytes);
                        ctx.writeAndFlush(wnb);
                        randomAccessFile.close();
                    }
                }
            }
        }
        else if(wnb.getUpOrDownLoadFlag().equals("download")){

        }
    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
