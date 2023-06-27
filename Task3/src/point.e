class
    POINT

create
    make
feature

    x: INTEGER
    y: INTEGER

    make (a_x, a_y: INTEGER; a_game_settings: GAME_SETTINGS)
        do
            check
                valid_x: a_x >= 0 and a_x < a_game_settings.cols
                valid_y: a_y >= 0 and a_y < a_game_settings.rows
            end
            x := a_x
            y := a_y
        end

end
