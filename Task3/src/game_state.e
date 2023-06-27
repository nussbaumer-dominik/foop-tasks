class
    GAME_STATE

create
    make
feature

    subway_systems: LIST[SUBWAY_SYSTEM]
    player: POINT
    game_settings: GAME_SETTINGS

    make (gs: GAME_SETTINGS)
    local
        random: RANDOM
        point_count: INTEGER
        i, j: INTEGER
        ss: SUBWAY_SYSTEM
        point: POINT
        random_col, random_row: INTEGER
    do
        game_settings := gs
        create player.make((gs.cols / 2).ceiling, (gs.rows / 2).ceiling, gs)

        create random.set_seed (123) -- actually random https://www.eiffel.org/article/random_numbers
        random.forth
        create {LINKED_LIST [SUBWAY_SYSTEM]} subway_systems.make

        from
            i := 1
        until
            i > gs.subway_systems
        loop
            create ss.make(gs.pipe_colors[subway_systems.count+1])
            point_count := random.item \\ (gs.max_subway_exits - 1) + 2
            random.forth

            from
                j := 1
            until
                j > point_count
            loop
                point:= new_unique_point(random, gs)
                ss.exits.extend(point)
                j := j + 1
            end
            subway_systems.extend(ss)
            i := i + 1
        end
    end

feature -- Player

    move_player(x_offset: INTEGER; y_offset: INTEGER)
        local
            new_x, new_y: INTEGER
        do
            new_x := player.x + x_offset
            new_y := player.y + y_offset

            if new_x >= 0 and new_x < game_settings.cols and
                new_y >= 0 and new_y < game_settings.rows then
            create player.make(new_x, new_y, game_settings)
            end
        end

feature -- Check for Exit at Point

    new_unique_point(random: RANDOM; gs: GAME_SETTINGS): POINT
       local
            point: POINT
            random_col, random_row: INTEGER
        do
           random_col := random.item \\ gs.cols
           random.forth
           random_row := random.item  \\ gs.rows
           random.forth
           create point.make(random_col, random_row, gs)

           from
           until
                equal(has_exit_at_point(point), ' ') and not equal(point, player)
           loop
               random_col := random.item \\ gs.cols
               random.forth
               random_row := random.item  \\ gs.rows
               random.forth
               create point.make(random_col, random_row, gs)
           end

           Result := point
        end

    has_exit_at_point(point: POINT): CHARACTER
        do
            Result := ' '
            across subway_systems as ss loop
                across ss.item.exits as exit loop
                    if equal(exit.item, point) then
                        Result := ss.item.color
                    end
                end
            end
        end
end
