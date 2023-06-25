class
    SUBWAY_SYSTEM

create
    make

feature
    exits: LIST[POINT]
    color: CHARACTER

    make (a_color: CHARACTER)
        do
            color := a_color
            io.put_character(color)
            io.put_string ("%N")

            create {LINKED_LIST [POINT]} exits.make
        end
end
