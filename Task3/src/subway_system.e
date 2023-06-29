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
            create {LINKED_LIST [MOUSE]} current_mouses.make
        end

feature
    enter(mouse: MOUSE)
        do
            current_mouses.extend(mouse)
        end

    leave(mouse: MOUSE)
        do
            from
            current_mouses.start  -- Set cursor to the first item.
            until
                current_mouses.after  -- Stop when we've checked all items.
            loop
                if current_mouses.item = mouse then  -- If the current item is what we're looking for...
                    current_mouses.remove  -- Remove it.
            end
                current_mouses.forth  -- Move to the next item.
            end
        end
end
