class
    GAME_STATE

create
    make
feature

    subway_systems: LIST[SUBWAY_SYSTEM]
    player: POINT
    mouses: LIST[MOUSE]
    game_settings: GAME_SETTINGS

    make (a_game_settings: GAME_SETTINGS)
    local
        random: RANDOM
    do
        game_settings := a_game_settings
        create random.set_seed (123) -- actually random https://www.eiffel.org/article/random_numbers
        random.forth

        init_player(random)
        init_subway_systems(random)
        init_mouses(random)

    end

feature -- Initialize Playing Field

init_player(random: RANDOM)
    do
        create player.make((game_settings.cols / 2).ceiling, (game_settings.rows / 2).ceiling, game_settings)
    end

init_subway_systems(random: RANDOM)
    local
        point_count: INTEGER
        i, j: INTEGER
        ss: SUBWAY_SYSTEM
        point: POINT
        uniqueness_criteria: FUNCTION [POINT, BOOLEAN]
    do
        create {LINKED_LIST [SUBWAY_SYSTEM]} subway_systems.make
        from
            i := 1
        until
            i > game_settings.subway_systems
        loop
            create ss.make(game_settings.pipe_colors[subway_systems.count+1])
            point_count := random.item \\ (game_settings.max_subway_exits - 1) + 2
            random.forth

            from
                j := 1
            until
                j > point_count
            loop
                point:= new_unique_point(random, game_settings, (agent (pt: POINT): BOOLEAN do
                    Result := equal(has_exit_at_point(pt), ' ') and not equal(pt, player)
                end))
                ss.exits.extend(point)
                j := j + 1
            end
            subway_systems.extend(ss)
            i := i + 1
        end
    end

init_mouses(random: RANDOM)
  local
    point: POINT
    i: INTEGER
    m: MOUSE
    uniqueness_criteria: FUNCTION [POINT, BOOLEAN]
  do
    create {LINKED_LIST [MOUSE]} mouses.make
    from
            i := 1
        until
            i > game_settings.mouses
        loop
            point:= new_unique_point(random, game_settings, (agent (pt: POINT): BOOLEAN do
                Result := equal(has_mouse_at_point(pt), ' ') and not equal(pt, player)
            end))
            create m.make(point)
            mouses.extend(m)

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

    new_unique_point(random: RANDOM; gs: GAME_SETTINGS; uniqueness_criteria: FUNCTION [POINT, BOOLEAN]): POINT
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
                uniqueness_criteria.item(point)
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

    has_mouse_at_point(point: POINT): CHARACTER
        do
            Result := ' '
            across mouses as m loop
                if equal(m.item.point, point) then
                    Result := 'm'
                end
            end
        end
end
