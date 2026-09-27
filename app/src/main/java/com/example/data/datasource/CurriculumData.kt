package com.example.data.datasource

import com.example.data.model.Exercise
import com.example.data.model.ExerciseType
import com.example.data.model.Lesson
import com.example.data.model.UnitData

object CurriculumData {

    fun getUnitsForLanguage(languageId: String): List<UnitData> {
        return when (languageId) {
            "gdscript" -> gdscriptUnits
            "lua" -> luaUnits
            "python" -> pythonUnits
            "csharp" -> csharpUnits
            "cpp" -> cppUnits
            "javascript" -> jsUnits
            "rust" -> rustUnits
            else -> gdscriptUnits
        }
    }

    // ==========================================
    // GDSCRIPT (GODOT 4) CURRICULUM
    // ==========================================
    private val gdscriptUnits = listOf(
        UnitData(
            unitNumber = 1,
            title = "Ünite 1: Godot 4 & Düğümler (Nodes)",
            description = "Godot'un temel yapı taşları, sahne ağacı ve _ready() fonksiyonu.",
            themeColorHex = "#478CBF",
            lessons = listOf(
                Lesson(
                    id = "gd_u1_l1",
                    unitNumber = 1,
                    title = "İlk Sahne ve _ready()",
                    subtitle = "Oyun başladığında çalışan ilk fonksiyon",
                    xpReward = 20,
                    gemReward = 10,
                    iconEmoji = "🎬",
                    exercises = listOf(
                        Exercise(
                            id = "gd_e1",
                            type = ExerciseType.WORD_BANK,
                            prompt = "Oyun başladığında ekrana 'Oyun Hazır!' yazdıran kodu oluştur:",
                            scrambledTokens = listOf("print(\"Oyun Hazır!\")", "func", "_ready():", "def", "var"),
                            correctTokenSequence = listOf("func", "_ready():", "print(\"Oyun Hazır!\")"),
                            explanation = "Godot'ta düğüm sahneye ilk girdiğinde 'func _ready():' tetiklenir ve altındaki kodlar çalışır."
                        ),
                        Exercise(
                            id = "gd_e2",
                            type = ExerciseType.FILL_IN_BLANK,
                            prompt = "Bir karakterin can değişkenini tanımlamak için boşluğu doldur:",
                            codeContext = "____ can: int = 100",
                            options = listOf("var", "let", "local", "int"),
                            correctOptionIndex = 0,
                            explanation = "GDScript dilinde değişken tanımlamak için 'var' anahtar kelimesi kullanılır."
                        ),
                        Exercise(
                            id = "gd_e3",
                            type = ExerciseType.MATCH_PAIRS,
                            prompt = "Godot terimlerini Türkçe karşılıklarıyla eşleştir:",
                            pairLeft = listOf("func _ready()", "var", "Vector2", "queue_free()"),
                            pairRight = listOf("Sahne yüklendiğinde çalışır", "Değişken tanımlar", "2 Boyutlu koordinat", "Nesneyi sahneden siler"),
                            explanation = "Eşleştirmeler Godot'un temel çalışma mantığını özetler."
                        ),
                        Exercise(
                            id = "gd_e4",
                            type = ExerciseType.MULTIPLE_CHOICE,
                            prompt = "GDScript'te bir nesneyi sahneden güvenli bir şekilde silip bellekten kaldırmak için ne çağrılır?",
                            options = listOf("queue_free()", "delete()", "destroy()", "remove()"),
                            correctOptionIndex = 0,
                            explanation = "Godot'ta nesneler 'queue_free()' ile mevcut karenin sonunda güvenle bellekten temizlenir."
                        )
                    )
                ),
                Lesson(
                    id = "gd_u1_l2",
                    unitNumber = 1,
                    title = "Oyun Döngüsü & _physics_process",
                    subtitle = "Her fizik karesinde karakteri hareket ettirme",
                    xpReward = 25,
                    gemReward = 15,
                    iconEmoji = "⚡",
                    exercises = listOf(
                        Exercise(
                            id = "gd_e5",
                            type = ExerciseType.WORD_BANK,
                            prompt = "Her fizik karesinde çalışan ana fonksiyon başlığını diz:",
                            scrambledTokens = listOf("_physics_process(delta):", "func", "update():", "def"),
                            correctTokenSequence = listOf("func", "_physics_process(delta):"),
                            explanation = "Fizik hesaplamaları ve karakter hareketi daima 'func _physics_process(delta):' içinde yapılır."
                        ),
                        Exercise(
                            id = "gd_e6",
                            type = ExerciseType.BUG_HUNT,
                            prompt = "Aşağıdaki GDScript kodundaki yazım hatasını (bug) bul:",
                            bugLines = listOf(
                                "func _physics_process(delta):",
                                "    var hiz = 200",
                                "    velocity.x = hiz",
                                "    move_and_slide" // Missing ()
                            ),
                            bugLineIndex = 3,
                            explanation = "'move_and_slide()' bir fonksiyondur, parantezleri () unutulmamalıdır!"
                        ),
                        Exercise(
                            id = "gd_e7",
                            type = ExerciseType.FILL_IN_BLANK,
                            prompt = "Kullanıcı 'ui_right' tuşuna bastığında doğru olan koşul hangisidir?",
                            codeContext = "if Input.____(\"ui_right\"):\n    velocity.x += 100",
                            options = listOf("is_action_pressed", "is_key_down", "get_pressed", "check_button"),
                            correctOptionIndex = 0,
                            explanation = "Godot'ta tuşa basılı tutulup tutulmadığını 'Input.is_action_pressed()' ile kontrol ederiz."
                        )
                    )
                )
            )
        ),
        UnitData(
            unitNumber = 2,
            title = "Ünite 2: Sinyaller ve Çarpışma",
            description = "Godot'un olay tabanlı mimarisi (Signals) ve CharacterBody2D.",
            themeColorHex = "#3273A8",
            lessons = listOf(
                Lesson(
                    id = "gd_u2_l1",
                    unitNumber = 2,
                    title = "Özel Sinyaller (Signals)",
                    subtitle = "Karakter öldüğünde veya altın toplandığında bildirim gönderme",
                    xpReward = 30,
                    gemReward = 15,
                    iconEmoji = "📡",
                    exercises = listOf(
                        Exercise(
                            id = "gd_e8",
                            type = ExerciseType.WORD_BANK,
                            prompt = "Karakter altın topladığında yayılan sinyali tanımla:",
                            scrambledTokens = listOf("signal", "altin_toplandi(miktar)", "event", "var"),
                            correctTokenSequence = listOf("signal", "altin_toplandi(miktar)"),
                            explanation = "GDScript'te özel sinyaller 'signal' anahtar kelimesi ve parametre listesi ile tanımlanır."
                        ),
                        Exercise(
                            id = "gd_e9",
                            type = ExerciseType.MULTIPLE_CHOICE,
                            prompt = "Tanımlı bir sinyali tetikleyip diğer dinleyicilere duyurmak için hangi komut kullanılır?",
                            options = listOf("altin_toplandi.emit(10)", "emit_event()", "send_signal()", "dispatch()"),
                            correctOptionIndex = 0,
                            explanation = "Godot 4'te sinyaller 'sinyal_adi.emit(...)' metoduyla tetiklenir."
                        )
                    )
                )
            )
        )
    )

    // ==========================================
    // LUA (ROBLOX & LOVE2D) CURRICULUM
    // ==========================================
    private val luaUnits = listOf(
        UnitData(
            unitNumber = 1,
            title = "Ünite 1: Roblox Studio & Lua Temelleri",
            description = "Roblox parçaları (Parts), Touched olayları ve yerel değişkenler.",
            themeColorHex = "#E03C31",
            lessons = listOf(
                Lesson(
                    id = "lua_u1_l1",
                    unitNumber = 1,
                    title = "Yerel Değişkenler & Konsol",
                    subtitle = "Roblox scriptlerinde 'local' kullanımı",
                    xpReward = 20,
                    gemReward = 10,
                    iconEmoji = "🧱",
                    exercises = listOf(
                        Exercise(
                            id = "lua_e1",
                            type = ExerciseType.WORD_BANK,
                            prompt = "Oyuncunun başlangıç altınını 50 olarak belirleyen Lua kodunu oluştur:",
                            scrambledTokens = listOf("local", "gold", "=", "50", "var", "let"),
                            correctTokenSequence = listOf("local", "gold", "=", "50"),
                            explanation = "Lua dilinde değişkenler en iyi performans ve kapsam yönetimi için 'local' ile tanımlanır."
                        ),
                        Exercise(
                            id = "lua_e2",
                            type = ExerciseType.FILL_IN_BLANK,
                            prompt = "Roblox konsoluna mesaj yazdırmak için boşluğu doldur:",
                            codeContext = "____(\"Roblox Oyununa Hoş Geldiniz!\")",
                            options = listOf("print", "echo", "console.log", "write"),
                            correctOptionIndex = 0,
                            explanation = "Lua'da ekrana ve Output penceresine yazdırmak için 'print()' fonksiyonu kullanılır."
                        ),
                        Exercise(
                            id = "lua_e3",
                            type = ExerciseType.MATCH_PAIRS,
                            prompt = "Lua ve Roblox terimlerini eşleştir:",
                            pairLeft = listOf("local", "script.Parent", "Touched:Connect", "nil"),
                            pairRight = listOf("Yerel değişken", "Scriptin bağlı olduğu parça", "Çarpışma dinleyicisi", "Boş / Değersiz"),
                            explanation = "Roblox Studio'da tüm parçalar hiyerarşik olarak bu yapı taşlarıyla kodlanır."
                        )
                    )
                ),
                Lesson(
                    id = "lua_u1_l2",
                    unitNumber = 1,
                    title = "Roblox Touched Çarpışma Olayı",
                    subtitle = "Lava bloğuna dokunan karaktere hasar verme",
                    xpReward = 25,
                    gemReward = 15,
                    iconEmoji = "🔥",
                    exercises = listOf(
                        Exercise(
                            id = "lua_e4",
                            type = ExerciseType.WORD_BANK,
                            prompt = "Parçaya dokunulduğunda çalışan fonksiyon bağlantısını kur:",
                            scrambledTokens = listOf("script.Parent.Touched:Connect(function(hit)", "end)", "start", "hook"),
                            correctTokenSequence = listOf("script.Parent.Touched:Connect(function(hit)", "end)"),
                            explanation = "Roblox'ta parçaların çarpışmasını algılamak için 'Touched:Connect(function(hit)...end)' kullanılır."
                        ),
                        Exercise(
                            id = "lua_e5",
                            type = ExerciseType.MULTIPLE_CHOICE,
                            prompt = "Dokunan nesnenin bir oyuncu karakteri olduğunu doğrulamak için hangi bileşen kontrol edilir?",
                            options = listOf("Humanoid", "PlayerController", "RigidBody", "BoxCollider"),
                            correctOptionIndex = 0,
                            explanation = "Roblox karakter modellerinde can ve hareketi yöneten ana bileşen 'Humanoid'dir."
                        )
                    )
                )
            )
        )
    )

    // ==========================================
    // PYTHON (PYGAME & GAME MECHANICS)
    // ==========================================
    private val pythonUnits = listOf(
        UnitData(
            unitNumber = 1,
            title = "Ünite 1: Pygame Temelleri & Ekran",
            description = "Pencere açma, oyun döngüsü ve FPS sabitleme.",
            themeColorHex = "#3776AB",
            lessons = listOf(
                Lesson(
                    id = "py_u1_l1",
                    unitNumber = 1,
                    title = "Pygame Başlatma & Pencere",
                    subtitle = "Oyun modüllerini yükleme ve ekran boyutu",
                    xpReward = 20,
                    gemReward = 10,
                    iconEmoji = "🕹️",
                    exercises = listOf(
                        Exercise(
                            id = "py_e1",
                            type = ExerciseType.WORD_BANK,
                            prompt = "Pygame kütüphanesini içeri aktarıp başlatan kod sırasını yap:",
                            scrambledTokens = listOf("import pygame", "pygame.init()", "start()", "run"),
                            correctTokenSequence = listOf("import pygame", "pygame.init()"),
                            explanation = "Tüm Pygame programları 'import pygame' ve ardından 'pygame.init()' çağrısı ile başlar."
                        ),
                        Exercise(
                            id = "py_e2",
                            type = ExerciseType.FILL_IN_BLANK,
                            prompt = "800x600 boyutunda oyun penceresi oluşturmak için boşluğu doldur:",
                            codeContext = "screen = pygame.display.____((800, 600))",
                            options = listOf("set_mode", "create_window", "open_screen", "make_display"),
                            correctOptionIndex = 0,
                            explanation = "Pygame ekranını oluşturmak için 'pygame.display.set_mode()' kullanılır."
                        ),
                        Exercise(
                            id = "py_e3",
                            type = ExerciseType.MATCH_PAIRS,
                            prompt = "Pygame kavramlarını görevleriyle eşleştir:",
                            pairLeft = listOf("pygame.init()", "clock.tick(60)", "pygame.QUIT", "colliderect()"),
                            pairRight = listOf("Modülleri başlatır", "60 FPS'e sabitler", "Pencere kapatma olayı", "İki kutunun çarpışması"),
                            explanation = "Pygame'in temel motor bileşenleri bu çağrılar üzerine inşa edilir."
                        )
                    )
                ),
                Lesson(
                    id = "py_u1_l2",
                    unitNumber = 1,
                    title = "Oyun Döngüsü & Olay Yakalama",
                    subtitle = "Klavye ve fare girdilerini while döngüsünde işleme",
                    xpReward = 25,
                    gemReward = 15,
                    iconEmoji = "🔄",
                    exercises = listOf(
                        Exercise(
                            id = "py_e4",
                            type = ExerciseType.WORD_BANK,
                            prompt = "Tüm bekleyen kullanıcı olaylarını dolaşan for döngüsünü kur:",
                            scrambledTokens = listOf("for", "event", "in", "pygame.event.get():", "list():"),
                            correctTokenSequence = listOf("for", "event", "in", "pygame.event.get():"),
                            explanation = "'pygame.event.get()' o karede gerçekleşen tuş basımları ve fare hareketlerini liste halinde verir."
                        ),
                        Exercise(
                            id = "py_e5",
                            type = ExerciseType.BUG_HUNT,
                            prompt = "Aşağıdaki Pygame kodundaki girinti veya sözdizimi hatasını bul:",
                            bugLines = listOf(
                                "running = True",
                                "while running:",
                                "for event in pygame.event.get():", // Unindented inside while!
                                "    if event.type == pygame.QUIT: running = False"
                            ),
                            bugLineIndex = 2,
                            explanation = "Python'da while döngüsünün içindeki kodlar bir kademe girintili (indent) olmalıdır!"
                        )
                    )
                )
            )
        )
    )

    // ==========================================
    // C# (UNITY) CURRICULUM
    // ==========================================
    private val csharpUnits = listOf(
        UnitData(
            unitNumber = 1,
            title = "Ünite 1: Unity & MonoBehaviour",
            description = "Unity bileşen mantığı, Start/Update ve Rigidbody fiziği.",
            themeColorHex = "#239120",
            lessons = listOf(
                Lesson(
                    id = "cs_u1_l1",
                    unitNumber = 1,
                    title = "Unity Yaşam Döngüsü",
                    subtitle = "Start() ve Update() farkı",
                    xpReward = 20,
                    gemReward = 10,
                    iconEmoji = "⚙️",
                    exercises = listOf(
                        Exercise(
                            id = "cs_e1",
                            type = ExerciseType.WORD_BANK,
                            prompt = "Her karede çağrılan Unity fonksiyonunu tanımla:",
                            scrambledTokens = listOf("void", "Update()", "{", "}", "Run()"),
                            correctTokenSequence = listOf("void", "Update()", "{", "}"),
                            explanation = "Unity'de 'Update()', oyun açık olduğu sürece her render karesinde çağrılır."
                        ),
                        Exercise(
                            id = "cs_e2",
                            type = ExerciseType.FILL_IN_BLANK,
                            prompt = "Karakterin fizik bileşenini koda bağlamak için boşluğu tamamla:",
                            codeContext = "Rigidbody2D rb = ____<Rigidbody2D>();",
                            options = listOf("GetComponent", "FindObject", "AddRigid", "LoadComponent"),
                            correctOptionIndex = 0,
                            explanation = "Bir GameObject'in üzerindeki bileşenlere erişmek için 'GetComponent<T>()' kullanılır."
                        )
                    )
                )
            )
        )
    )

    // ==========================================
    // C++ (UNREAL ENGINE) CURRICULUM
    // ==========================================
    private val cppUnits = listOf(
        UnitData(
            unitNumber = 1,
            title = "Ünite 1: Unreal Engine C++ & AActor",
            description = "Oyun nesneleri (Actors), Tick fonksiyonu ve işaretçiler (Pointers).",
            themeColorHex = "#00599C",
            lessons = listOf(
                Lesson(
                    id = "cpp_u1_l1",
                    unitNumber = 1,
                    title = "Actor Sınıfı & Tick",
                    subtitle = "DeltaTime ile kare hızından bağımsız hareket",
                    xpReward = 25,
                    gemReward = 15,
                    iconEmoji = "🚀",
                    exercises = listOf(
                        Exercise(
                            id = "cpp_e1",
                            type = ExerciseType.WORD_BANK,
                            prompt = "Unreal Actor sınıfında her kare çalışan fonksiyon başlığını kur:",
                            scrambledTokens = listOf("virtual", "void", "Tick(float DeltaTime)", "override;"),
                            correctTokenSequence = listOf("virtual", "void", "Tick(float DeltaTime)", "override;"),
                            explanation = "Unreal Engine'de Actor her kare güncellenirken 'Tick(float DeltaTime)' metodu çalışır."
                        ),
                        Exercise(
                            id = "cpp_e2",
                            type = ExerciseType.MULTIPLE_CHOICE,
                            prompt = "Unreal editöründe bir değişkeni Blueprint'lerde görünür kılmak için hangi makro eklenir?",
                            options = listOf("UPROPERTY(EditAnywhere)", "UFUNCTION()", "USTRUCT()", "UINTERFACE()"),
                            correctOptionIndex = 0,
                            explanation = "'UPROPERTY(EditAnywhere)' makrosu C++ değişkenlerini Unreal Engine editör paneline taşır."
                        )
                    )
                )
            )
        )
    )

    // ==========================================
    // JAVASCRIPT (PHASER.JS & WEB GAMES)
    // ==========================================
    private val jsUnits = listOf(
        UnitData(
            unitNumber = 1,
            title = "Ünite 1: Phaser.js & 2D Web Oyunları",
            description = "Tarayıcıda preload, create, update ve Arcade Physics.",
            themeColorHex = "#F7DF1E",
            lessons = listOf(
                Lesson(
                    id = "js_u1_l1",
                    unitNumber = 1,
                    title = "Phaser Sahne Metotları",
                    subtitle = "Görselleri önbelleğe alıp sahneye ekleme",
                    xpReward = 20,
                    gemReward = 10,
                    iconEmoji = "🌐",
                    exercises = listOf(
                        Exercise(
                            id = "js_e1",
                            type = ExerciseType.WORD_BANK,
                            prompt = "Karakter sprite'ını fizik motoruyla sahneye ekleyen kodu kur:",
                            scrambledTokens = listOf("this.physics.add.sprite(100,", "450,", "'player');"),
                            correctTokenSequence = listOf("this.physics.add.sprite(100,", "450,", "'player');"),
                            explanation = "Phaser Arcade Physics ile sprite eklerken 'this.physics.add.sprite(x, y, key)' kullanılır."
                        ),
                        Exercise(
                            id = "js_e2",
                            type = ExerciseType.FILL_IN_BLANK,
                            prompt = "Oyun görsellerini indirmek için hangi sahne fonksiyonu kullanılır?",
                            codeContext = "function ____() {\n    this.load.image('sky', 'assets/sky.png');\n}",
                            options = listOf("preload", "create", "update", "render"),
                            correctOptionIndex = 0,
                            explanation = "Phaser'da resim, ses ve harita varlıkları 'preload()' aşamasında indirilir."
                        )
                    )
                )
            )
        )
    )

    // ==========================================
    // RUST (BEVY ENGINE ECS)
    // ==========================================
    private val rustUnits = listOf(
        UnitData(
            unitNumber = 1,
            title = "Ünite 1: Bevy Engine & ECS Mimarisi",
            description = "Entity-Component-System yapısı ile sıfır maliyetli oyun mantığı.",
            themeColorHex = "#DEA584",
            lessons = listOf(
                Lesson(
                    id = "rust_u1_l1",
                    unitNumber = 1,
                    title = "Bileşenler (Components) & Sorgular",
                    subtitle = "Struct ile bileşen tanımlama",
                    xpReward = 25,
                    gemReward = 15,
                    iconEmoji = "🦀",
                    exercises = listOf(
                        Exercise(
                            id = "rust_e1",
                            type = ExerciseType.WORD_BANK,
                            prompt = "Bevy bileşeni olarak işaretlenen can struct'ını kur:",
                            scrambledTokens = listOf("#[derive(Component)]", "struct", "Health(f32);"),
                            correctTokenSequence = listOf("#[derive(Component)]", "struct", "Health(f32);"),
                            explanation = "Bevy'de bir struct'ı oyun bileşeni yapmak için '#[derive(Component)]' türetimi yapılır."
                        ),
                        Exercise(
                            id = "rust_e2",
                            type = ExerciseType.FILL_IN_BLANK,
                            prompt = "Tüm oyuncu konumlarını dolaşmak için sistem parametresindeki boşluğu doldur:",
                            codeContext = "fn move_player(mut query: ____<&mut Transform>) {\n}",
                            options = listOf("Query", "List", "Vector", "Collection"),
                            correctOptionIndex = 0,
                            explanation = "Bevy sistemleri dünyadaki varlıkları 'Query<...>' tipi ile sorgular ve filtreler."
                        )
                    )
                )
            )
        )
    )
}
