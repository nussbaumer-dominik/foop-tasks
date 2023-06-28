class
    CONSOLE_OUTPUT_MEDIUM
create
    make

feature

    game_settings: GAME_SETTINGS

    make (a_game_settings: GAME_SETTINGS)
        do
            game_settings := a_game_settings
        end

    draw (game_state: GAME_STATE)
        local
            i: INTEGER
            j: INTEGER
            point: POINT
            c: CHARACTER
        do
            clear_screen
            draw_line("---")
            from
                i := 0
            until
                i > game_settings.rows -1
            loop
                io.put_string (" | ")
                from
                    j := 0
                until
                    j > game_settings.cols -1
                loop
                    create point.make(j, i, game_settings)
                    c := game_state.has_exit_at_point(point)
                    if not game_settings.show_pipes_color and not equal(c, ' ') then
                        c := 'o'
                    end
                    if equal(game_state.player, point) then
                        c := 'c'
                    end
                    io.put_character(' ')
                    io.put_character(c)
                    io.put_character(' ')

                    j := j + 1
                end
                io.put_string (" | ")
                io.put_string ("%N")
                i := i + 1
            end
            draw_line("---")
        end

    draw_line(segment: STRING)
        local
            i: INTEGER
        do
            from
                i := 1 - 2
            until
                i > game_settings.cols
            loop
                io.put_string (segment)
                i := i + 1
            end
            io.put_string ("%N")
        end

    clear_screen
        local
            i: INTEGER
        do
            from
                i := 1
            until
                i > game_settings.rows
            loop
                io.put_string ("%N")
                i := i + 1
            end
        end
end