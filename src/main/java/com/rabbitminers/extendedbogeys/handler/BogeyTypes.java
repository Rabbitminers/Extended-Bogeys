package com.rabbitminers.extendedbogeys.handler;

public enum BogeyTypes {
    DEFAULT(1, "Default"),
    SIXWHEEL(2, "Six wheels");
    ;
    private final int id;
    private final String name;

    BogeyTypes(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BogeyTypes getBogeyTypeOfId(int id) {
        for (BogeyTypes bogeyType : BogeyTypes.values())
            if (bogeyType.getId() == id)
                return bogeyType;
        return null;
    }
}
