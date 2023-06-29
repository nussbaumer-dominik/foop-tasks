class
    APPLICATION

inherit
    ARGUMENTS_32

create
    make

feature {NONE}

    make
        local
            game_settings: GAME_SETTINGS
            game_state: GAME_STATE
            output: CONSOLE_OUTPUT_MEDIUM
        do
            create game_settings.make
            create game_state.make(game_settings)
            create output.make(game_settings)
            from
                output.draw(game_state)
                io.read_line
            until
                equal (io.last_string, "q")
            loop
                if equal (io.last_string, "c") then
                    game_settings.toggle_show_pipes_color
                end

                if equal (io.last_string, "w") then
                    game_state.move_player(0, -1)
                end
                if equal (io.last_string, "s") then
                    game_state.move_player(0, 1)
                end
                if equal (io.last_string, "a") then
                    game_state.move_player(-1, 0)
                end
                if equal (io.last_string, "d") then
                    game_state.move_player(1, 0)
                end
                output.draw(game_state)
                game_state.move_mouses
                game_state.catch
                io.put_string ("Current score: ")
                io.put_integer (game_state.score)
                io.put_string ("; You pressed: ")
                io.put_string (io.last_string)
                io.put_new_line
                io.read_line
            end
        end

    end
