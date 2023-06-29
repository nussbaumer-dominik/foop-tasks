class
    MOUSE

create
    make
feature

    point: POINT
    current_subway: detachable SUBWAY_SYSTEM
    target: POINT


    make (a_point: POINT; target_point: POINT)
        do
            point := a_point
            target := target_point
        end

    is_above_ground: BOOLEAN
    do
        if attached current_subway as attached_subway then
            Result := False
        else
            Result := True
        end
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

    move_to_target
    local
        random: RANDOM
        moved: BOOLEAN
    do
        moved := False
        create random.set_seed (point.x + point.y)
        random.forth
        if random.item \\ 2 = 0 then
            if point.x < target.x then
                moved:= TRUE
                point.move(1, 0)
            end
            if point.x > target.x then
                moved:= TRUE
                point.move(-1, 0)
            end
            if not moved then
                if point.y < target.y then
                    moved:= TRUE
                    point.move(0, 1)
                end
                if point.y > target.y then
                    moved:= TRUE
                    point.move(0, -1)
                end
            end
        else
            if point.y < target.y then
                moved:= TRUE
                point.move(0, 1)
            end
            if point.y > target.y then
                moved:= TRUE
                point.move(0, -1)
            end
            if not moved then
                if point.x < target.x then
                    moved:= TRUE
                    point.move(1, 0)
                end
                if point.x > target.x then
                    moved:= TRUE
                    point.move(-1, 0)
                end
            end
        end

    end
end
