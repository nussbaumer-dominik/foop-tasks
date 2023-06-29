class
    GAME_STATE

create
    make
feature

    goal_system: SUBWAY_SYSTEM
    subway_systems: LIST[SUBWAY_SYSTEM]
    player: POINT
    mouses: LIST[MOUSE]
    game_settings: GAME_SETTINGS
    score: INTEGER

    make (a_game_settings: GAME_SETTINGS)
    local
        random: RANDOM
        true_random: TRUE_RANDOM
    do
        score := 0
    	create {LINKED_LIST [MOUSE]} mouses.make
        game_settings := a_game_settings
        create true_random.make
        random := true_random.random

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
        goal_system_index: INTEGER;
        i, j: INTEGER
        ss: SUBWAY_SYSTEM
        point: POINT
        uniqueness_criteria: FUNCTION [POINT, BOOLEAN]
    do
        create {LINKED_LIST [SUBWAY_SYSTEM]} subway_systems.make
        goal_system_index := random.item \\ game_settings.subway_systems
        random.forth
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
            if goal_system_index = i then
               goal_system := ss
            end
            subway_systems.extend(ss)
            i := i + 1
        end
    end

init_mouses(random: RANDOM)
  local
    point: POINT
    target: POINT
    i: INTEGER
    m: MOUSE
    uniqueness_criteria: FUNCTION [POINT, BOOLEAN]
    choosen_exit_index: INTEGER
  do
    choosen_exit_index := random.item // goal_system.exits.count
    random.forth

    target := goal_system.exits.i_th(choosen_exit_index)
    from
            i := 1
        until
            i > game_settings.mouses
        loop
            point:= new_unique_point(random, game_settings, (agent (pt: POINT): BOOLEAN do
                Result := equal(has_mouse_at_point(pt), ' ') and not equal(pt, player)
            end))
            create m.make(point, target)
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

    has_mouse_at_overworld_point(point: POINT): CHARACTER
        do
            Result := ' '
            across mouses as m loop
                if equal(m.item.point, point) and m.item.is_above_ground then
                    Result := 'm'
                end
            end
        end

    catch
     do
        across mouses as m loop
            if equal(m.item.point, player) and m.item.is_above_ground then
                m.item.exit
                score := score +1
                from
                    mouses.start  -- Set cursor to the first item.
                until
                    mouses.after  -- Stop when we've checked all items.
                loop
                    if mouses.item = m.item then  -- If the current item is what we're looking for...
                        mouses.remove  -- Remove it.
                    end
                    mouses.forth  -- Move to the next item.
                end      
            end
        end
    end

    move_mouses
    do
        across mouses as m loop
            m.item.move_to_target
        end
    end
end
