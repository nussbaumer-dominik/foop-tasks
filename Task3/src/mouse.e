class
    MOUSE

create
    make
feature

    point: POINT
    current_subway: detachable SUBWAY_SYSTEM

    make (a_point: POINT)
        do
            point := a_point
        end

    enter (subway: SUBWAY_SYSTEM)
    do
        subway.enter(Current)
        current_subway := subway
    end

    exit
    do
        if attached current_subway as attached_subway then
            attached_subway.leave(Current)
            current_subway := Void
        end

    end
end
