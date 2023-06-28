class
    SUBWAY_SYSTEM

create
    make

feature
    exits: LIST[POINT]
    color: CHARACTER
    current_mouses: LIST[MOUSE]

    make (a_color: CHARACTER)
        do
            color := a_color
            io.put_character(color)
            io.put_string ("%N")

            create {LINKED_LIST [POINT]} exits.make
        end

feature {MOUSE}
    private enter(mouse: MOUSE)
        do
            current_mouses.extend(mouse)
        end
end
