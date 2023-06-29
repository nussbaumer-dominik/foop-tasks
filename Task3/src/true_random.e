class
    TRUE_RANDOM
create
    make
feature
    random: RANDOM

    make
        local
        do
            create random.set_seed (12345)
            random.forth
        end
end