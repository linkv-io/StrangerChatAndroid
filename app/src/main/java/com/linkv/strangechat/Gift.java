package com.linkv.strangechat;

/**
 * Created by Xiaohong on 2020/11/9.
 * desc:
 */
public class Gift {
    private String name;
    private int id;
    private boolean isStatic;
    private int previewId;
    private int animId;
    private int price;

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public boolean isStatic() {
        return isStatic;
    }

    public int getPreviewId() {
        return previewId;
    }

    public int getAnimId() {
        return animId;
    }

    private Gift(int id) {
        this.id = id;
    }


    public static Gift createGiftById(int id) {
        Gift gift = new Gift(id);
        switch (id) {
            case 0:
                gift.name = "飞机";
                gift.previewId = R.drawable.airplane;
                gift.animId = R.drawable.anim_gift_plane;
                gift.isStatic = false;
                break;
            case 1:
                gift.name = "城堡";
                gift.previewId = R.drawable.castle;
                gift.animId = R.drawable.anim_gift_castle;
                gift.isStatic = false;
                break;

            case 2:
                gift.name = "天使";
                gift.previewId = R.drawable.angel;
                gift.animId = R.drawable.anim_gift_angel;
                gift.isStatic = false;
                break;
            case 3:
                gift.name = "豪轮";
                gift.previewId = R.drawable.boat;
                gift.animId = R.drawable.anim_gift_boat;
                gift.isStatic = false;
                break;
            case 4:
                gift.name = "咖啡";
                gift.previewId = R.drawable.coffee3x;
                gift.isStatic = true;
                break;
            case 5:
                gift.name = "四叶草";
                gift.previewId = R.drawable.siyecao3x;
                gift.isStatic = true;
                break;
            case 6:
                gift.name = "小熊";
                gift.previewId = R.drawable.beer3x;
                gift.isStatic = true;
                break;
            case 7:
                gift.name = "棒棒糖";
                gift.previewId = R.drawable.tang3x;
                gift.isStatic = true;
                break;
        }
        return gift;
    }

}
