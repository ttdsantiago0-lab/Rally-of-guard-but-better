# ARMAS_SCGUNS — valores reales de las armas de Scorched Guns 1.5 (leídos del jar, 2026-11-04)

Para el torneo (docs/TORNEO_DOCTRINAS.md §3, §8): cada arma se valora por lo que hace en el juego, no por su nombre.
`dps` = daño × proyectiles × 20 / cadencia (ticks entre disparos), sin recarga ni dispersión; `alcance` ≈ velocidad × vida del
proyectil (bloques, techo teórico). Familias = `weaponType` del propio mod.

> **Ojo:** son techos teóricos. Valores extremos (p. ej. escopetas de miles de dps) salen de cadencias de 0–1 tick o de
> muchos perdigones con mucha dispersión: el torneo mide su valor real en combate; esta tabla solo decide qué probar.

| Familia | n | dps mediana | alcance mediano | armas (dps) |
|---|---|---|---|---|
| rifle | 27 | 40 | 360 | locust (210), nervepinch (100), drill (90), teslock_rifle (74), lockewood (73), vulcanic_repeater (70), zilk_45 (65), sculk_resonator (58) … |
| shotgun | 25 | 41 | 175 | drill_conversion (4230), jr_wristbreaker (2145), kiln_gun (1904), super_shotgun (460), fencer_thumper (275), newborn_cyst (88), callwell (80), jackhammer (65) … |
| pistol | 17 | 37 | 240 | whizzbanger (88), prima_materia (82), hyperbaria (62), osgood_50 (60), raygun (58), auvtomag (50), krauser (48), defender_pistol (42) … |
| sniper | 14 | 34 | 615 | stiletto (90), big_bore (74), saketini (48), mas_peddler (41), pyroclastic_flow (38), astella (37), mas_55 (36), whistler (32) … |
| heavy | 11 | 77 | 200 | terra_incognita (175), blooper (153), rocket_rifle (147), truant (92), weevil (78), scratches (77), thunderhead (52), cyclone (44) … |
| smg | 11 | 45 | 150 | rat_king_and_queen (153), prush_gun (70), freyr (63), soul_drummer (60), greaser_smg (50), mak_mkii (45), carapice (43), rusty_gnat (33) … |
| lmg | 7 | 58 | 428 | howler_conversion (163), earths_corpse (135), gattaler (65), spirulida (58), cogloader (50), stigg (50), birdfeeder (38) |
| magnum | 6 | 80 | 345 | micina (103), ultra_knight_hawk (87), m22_waltz (82), uppercut (77), bruiser (39), sequoia (33) |
| laser | 6 | 36 | 178 | flayed_god (80), waltz_conversion (43), shard_culler (40), minksy (32), laser_musket (25), cr4k_mining_laser (5) |
| plasma | 6 | 71 | 236 | libertas (336), sterilizer (108), plasmabuss (78), gauss_rifle (64), plasgun (42), tl_runner (26) |
| flamethrower | 4 | 45 | 31 | inquisitor (373), spitfire (60), basker (30), blasphemy (29) |
| special | 4 | 38 | 198 | bomb_lance (58), hullbreaker (58), niami (18), crusader (0) |
| shock | 3 | 30 | 320 | lone_wonder (92), pulsar (30), arc_worker (20) |

## Tabla completa

| Arma | Familia | Daño | Proyectiles | Cadencia (ticks) | dps | Dispersión | Alcance | Cargador | Recarga (ticks) | Munición | Modo |
|---|---|---|---|---|---|---|---|---|---|---|---|
| inquisitor | flamethrower | 8 | 7 | 3 | 373.3 | 15 | 10 | 21 | 30 | blaze_fuel | burst |
| spitfire | flamethrower | 9.0 | 1 | 3 | 60.0 | 10.0 | 22 | 32 | 30 | blaze_fuel | automatic |
| basker | flamethrower | 6.0 | 1 | 4 | 30.0 | 2.0 | 45 | 15 | 30 | blaze_fuel | automatic |
| blasphemy | flamethrower | 10.0 | 1 | 7 | 28.6 | 5.0 | 40 | 10 | 30 | blaze_fuel | automatic |
| terra_incognita | heavy | 35.0 | 1 | 4 | 175.0 | 2.0 | 302 | 8 | 20 | rocket | automatic |
| blooper | heavy | 23 | 1 | 3 | 153.3 | 2.5 | 165 | 1 | 40 | he_grenade_round | semi_automatic |
| rocket_rifle | heavy | 22.0 | 1 | 3 | 146.7 | 2.5 | 220 | 1 | 40 | rocket | semi_automatic |
| truant | heavy | 23 | 1 | 5 | 92.0 | 2.5 | 165 | 6 | 40 | he_grenade_round | semi_automatic |
| weevil | heavy | 15.5 | 1 | 4 | 77.5 | 3.0 | 90 | 25 | 60 | shulkshot | burst |
| scratches | heavy | 23.0 | 1 | 6 | 76.7 | 9.25 | 200 | 24 | 30 | nitro_buckshot | automatic |
| thunderhead | heavy | 13.0 | 1 | 5 | 52.0 | 1.5 | 300 | 35 | 60 | shatter_round | automatic |
| cyclone | heavy | 15.5 | 1 | 7 | 44.3 | 3.0 | 75 | 21 | 15 | microjet | automatic |
| dozier_rl | heavy | 28.0 | 1 | 13 | 43.1 | 3.0 | 231 | 4 | 20 | rocket | semi_automatic |
| triquetra | heavy | 23 | 1 | 14 | 32.9 | 2.5 | 192 | 3 | 40 | he_grenade_round | semi_automatic |
| hammer_gl | heavy | 24.5 | 1 | 17 | 28.8 | 2.5 | 248 | 4 | 40 | he_grenade_round | semi_automatic |
| flayed_god | laser | 12.0 | 1 | 3 | 80.0 | 3.0 | 75 | 55 | 30 | energy_core | beam |
| waltz_conversion | laser | 15.0 | 1 | 7 | 42.9 | 2.0 | 280 | 12 | 20 | energy_core | semi_beam |
| shard_culler | laser | 6.0 | 1 | 3 | 40.0 | 3.0 | 75 | 50 | 30 | energy_core | beam |
| minksy | laser | 16 | 1 | 10 | 32.0 | 0.1 | 280 | 15 | 27 | energy_core | semi_beam |
| laser_musket | laser | 15 | 1 | 12 | 25.0 | 0.1 | 280 | 7 | 27 | energy_core | semi_beam |
| cr4k_mining_laser | laser | 1.0 | 1 | 4 | 5.0 | 3.0 | 75 | 150 | 35 | energy_core | beam |
| howler_conversion | lmg | 24.5 | 1 | 3 | 163.3 | 4.2 | 845 | 9 | 30 | krahg_round | automatic |
| earths_corpse | lmg | 13.5 | 1 | 2 | 135.0 | 5.0 | 275 | 75 | 50 | advanced_round | automatic |
| gattaler | lmg | 6.5 | 1 | 2 | 65.0 | 3.0 | 200 | 90 | 60 | compact_advanced_round | automatic |
| spirulida | lmg | 11.5 | 1 | 4 | 57.5 | 2 | 550 | 27 | 70 | advanced_round | automatic |
| cogloader | lmg | 10.0 | 1 | 4 | 50.0 | 1.6 | 325 | 46 | 35 | advanced_round | automatic |
| stigg | lmg | 10.0 | 1 | 4 | 50.0 | 2.0 | 428 | 31 | 125 | advanced_round | automatic |
| birdfeeder | lmg | 7.5 | 1 | 4 | 37.5 | 4.0 | 500 | 16 | 40 | standard_copper_round | automatic |
| micina | magnum | 15.5 | 1 | 3 | 103.3 | 5.0 | 175 | 6 | 18 | ramrod_round | burst |
| ultra_knight_hawk | magnum | 65.0 | 1 | 15 | 86.7 | 0.0 | 560 | 3 | 20 | osborne_slug | semi_automatic |
| m22_waltz | magnum | 16.5 | 1 | 4 | 82.5 | 2.0 | 360 | 8 | 35 | ramrod_round | semi_automatic |
| uppercut | magnum | 23.0 | 1 | 6 | 76.7 | 2.0 | 330 | 5 | 20 | beowulf_round | semi_automatic |
| bruiser | magnum | 13.5 | 1 | 7 | 38.6 | 1.2 | 248 | 4 | 10 | ramrod_round | semi_automatic |
| sequoia | magnum | 20.0 | 1 | 12 | 33.3 | 3.0 | 560 | 6 | 20 | beowulf_round | semi_automatic |
| whizzbanger | pistol | 88 | 1 | 20 | 88.0 | 20 | 1400 | 1 | 70 | osborne_slug | semi_automatic |
| prima_materia | pistol | 16.5 | 1 | 4 | 82.5 | 1.0 | 405 | 14 | 50 | compact_advanced_round | semi_automatic |
| hyperbaria | pistol | 12.5 | 1 | 4 | 62.5 | 0.5 | 180 | 10 | 50 | frog_dart | semi_automatic |
| osgood_50 | pistol | 24.0 | 1 | 8 | 60.0 | 1.0 | 330 | 6 | 10 | beowulf_round | semi_automatic |
| raygun | pistol | 23.0 | 1 | 8 | 57.5 | 2.0 | 250 | 17 | 25 | energy_cell | automatic |
| auvtomag | pistol | 15.0 | 1 | 6 | 50.0 | 1.0 | 300 | 6 | 50 | ramrod_round | semi_automatic |
| krauser | pistol | 9.5 | 1 | 4 | 47.5 | 2.0 | 280 | 13 | 20 | compact_advanced_round | semi_automatic |
| defender_pistol | pistol | 8.5 | 1 | 4 | 42.5 | 1.5 | 315 | 12 | 50 | compact_advanced_round | semi_automatic |
| mokova | pistol | 5.5 | 1 | 3 | 36.7 | 1.5 | 180 | 8 | 50 | compact_copper_round | semi_automatic |
| flintlock_pistol | pistol | 10.0 | 1 | 6 | 33.3 | 3.0 | 200 | 1 | 45 | powder_and_ball | semi_automatic |
| railworker | pistol | 6.5 | 1 | 4 | 32.5 | 2 | 270 | 12 | 20 | needle | semi_automatic |
| scrapper | pistol | 5.5 | 1 | 4 | 27.5 | 2.0 | 180 | 8 | 40 | compact_copper_round | semi_automatic |
| trenchur | pistol | 9.5 | 1 | 7 | 27.1 | 3.0 | 175 | 6 | 18 | compact_advanced_round | semi_automatic |
| gyrojet_pistol | pistol | 4.0 | 1 | 5 | 16.0 | 3.0 | 88 | 6 | 25 | microjet | semi_automatic |
| umax_pistol | pistol | 6.0 | 1 | 8 | 15.0 | 0.1 | 240 | 7 | 15 | standard_bullet | semi_automatic |
| pax | pistol | 7.5 | 1 | 13 | 11.5 | 3.0 | 175 | 6 | 18 | compact_copper_round | semi_automatic |
| longarm | pistol | 6.5 | 1 | 17 | 7.6 | 3.5 | 140 | 5 | 18 | powder_and_ball | semi_automatic |
| libertas | plasma | 21 | 4 | 5 | 336.0 | 5 | 225 | 8 | 30 | energy_cell | semi_automatic |
| sterilizer | plasma | 21.5 | 1 | 4 | 107.5 | 1 | 375 | 18 | 90 | energy_cell | automatic |
| plasmabuss | plasma | 23.5 | 1 | 6 | 78.3 | 4.0 | 225 | 7 | 15 | energy_cell | semi_automatic |
| gauss_rifle | plasma | 22.5 | 1 | 7 | 64.3 | 3.5 | 900 | 8 | 30 | energy_cell | pulse |
| plasgun | plasma | 17.0 | 1 | 8 | 42.5 | 4.0 | 248 | 15 | 35 | energy_cell | automatic |
| tl_runner | plasma | 14.5 | 1 | 11 | 26.4 | 3.0 | 131 | 5 | 50 | energy_cell | automatic |
| locust | rifle | 21.0 | 1 | 2 | 210.0 | 2.0 | 700 | 24 | 15 | gibbs_round | burst |
| nervepinch | rifle | 30.0 | 1 | 6 | 100.0 | 0.1 | 360 | 8 | 30 | minecraft:blaze_rod | pulse |
| drill | rifle | 13.5 | 1 | 3 | 90.0 | 1 | 240 | 2 | 50 | powder_and_ball | semi_automatic |
| teslock_rifle | rifle | 13 | 2 | 7 | 74.3 | 3 | 320 | 5 | 30 | shock_cell | pulse |
| lockewood | rifle | 11.0 | 1 | 3 | 73.3 | 2.0 | 578 | 21 | 90 | advanced_round | burst |
| vulcanic_repeater | rifle | 14.0 | 1 | 4 | 70.0 | 1.2 | 248 | 15 | 35 | hog_round | automatic |
| zilk_45 | rifle | 13 | 1 | 4 | 65.0 | 0.75 | 225 | 13 | 125 | frog_dart | automatic |
| sculk_resonator | rifle | 23.0 | 1 | 8 | 57.5 | 3.0 | 1100 | 8 | 30 | sculk_cell | pulse |
| rg_jigsaw | rifle | 14.0 | 1 | 5 | 56.0 | 1.5 | 248 | 16 | 125 | shatter_round | automatic |
| m3_carabine | rifle | 11.0 | 1 | 4 | 55.0 | 1.5 | 428 | 19 | 125 | advanced_round | semi_automatic |
| gale | rifle | 16.0 | 1 | 6 | 53.3 | 0.1 | 900 | 8 | 30 | hardened_bullet | pulse |
| kalaskah | rifle | 7.0 | 1 | 3 | 46.7 | 3.0 | 525 | 18 | 90 | standard_copper_round | burst |
| grandle_og | rifle | 8.5 | 1 | 4 | 42.5 | 1.0 | 750 | 7 | 40 | compact_advanced_round | semi_automatic |
| musket | rifle | 12.0 | 1 | 6 | 40.0 | 1.0 | 280 | 1 | 50 | powder_and_ball | semi_automatic |
| dark_matter | rifle | 16.5 | 1 | 9 | 36.7 | 3.0 | 660 | 38 | 8 | advanced_round | automatic |
| m3_marksman | rifle | 11.0 | 1 | 6 | 36.7 | 1.5 | 248 | 10 | 125 | shatter_round | semi_automatic |
| grandle | rifle | 8.5 | 1 | 5 | 34.0 | 1.0 | 750 | 17 | 40 | compact_advanced_round | automatic |
| mk43_rifle | rifle | 14.0 | 1 | 9 | 31.1 | 2.0 | 160 | 10 | 25 | microjet | automatic |
| valora | rifle | 9.0 | 1 | 6 | 30.0 | 2.0 | 550 | 17 | 20 | compact_advanced_round | automatic |
| makeshift_rifle | rifle | 8.5 | 1 | 6 | 28.3 | 3.0 | 320 | 16 | 90 | standard_copper_round | semi_automatic |
| marlin | rifle | 15.0 | 1 | 12.0 | 25.0 | 3.0 | 495 | 10 | 15 | advanced_round | semi_automatic |
| iron_javelin | rifle | 15.5 | 1 | 14 | 22.1 | 1.0 | 750 | 9 | 40 | advanced_round | semi_automatic |
| llr_director | rifle | 13.0 | 1 | 16 | 16.2 | 2.0 | 500 | 7 | 40 | standard_copper_round | semi_automatic |
| winnie_millend | rifle | 10.5 | 1 | 14 | 15.0 | 2 | 300 | 6 | 22 | standard_copper_round | semi_automatic |
| winnie | rifle | 8 | 1 | 12 | 13.3 | 1.0 | 300 | 8 | 22 | compact_copper_round | semi_automatic |
| fencer_carabine | rifle | 9 | 1 | 19 | 9.5 | 1 | 175 | 7 | 22 | powder_and_ball | semi_automatic |
| red_raydar | rifle | 1 | 1 | 11 | 1.8 | 1 | 160 | 10 | 15 | standard_bullet | semi_automatic |
| lone_wonder | shock | 23.0 | 1 | 5 | 92.0 | 3.0 | 320 | 23 | 30 | shock_cell | automatic |
| pulsar | shock | 7.5 | 1 | 5 | 30.0 | 2.0 | 280 | 4 | 15 | shock_cell | semi_automatic |
| arc_worker | shock | 8.0 | 1 | 8 | 20.0 | 1.0 | 320 | 10 | 30 | shock_cell | semi_automatic |
| drill_conversion | shotgun | 23.5 | 18 | 2 | 4230.0 | 7.5 | 132 | 2 | 50 | shotgun_shell | semi_automatic |
| jr_wristbreaker | shotgun | 19.5 | 22 | 4 | 2145.0 | 10.5 | 132 | 12 | 30 | shotgun_shell | automatic |
| kiln_gun | shotgun | 28 | 17 | 5 | 1904.0 | 9 | 140 | 2 | 100 | bearpack_shell | semi_automatic |
| super_shotgun | shotgun | 23.0 | 1 | 1 | 460.0 | 4.0 | 160 | 2 | 20 | shotgun_shell | burst |
| fencer_thumper | shotgun | 16.5 | 20 | 24 | 275.0 | 8 | 175 | 4 | 100 | grapeshot | semi_automatic |
| newborn_cyst | shotgun | 35 | 1 | 8 | 87.5 | 7.0 | 240 | 6 | 6 | bearpack_shell | semi_automatic |
| callwell | shotgun | 16.0 | 1 | 4 | 80.0 | 6.25 | 225 | 2 | 30 | shotgun_shell | semi_automatic |
| jackhammer | shotgun | 19.5 | 1 | 6 | 65.0 | 12.0 | 132 | 12 | 30 | shotgun_shell | automatic |
| boomstick | shotgun | 20.0 | 1 | 7 | 57.1 | 10.0 | 125 | 6 | 60 | shotgun_shell | semi_automatic |
| blunderbuss | shotgun | 16.5 | 1 | 6 | 55.0 | 9.0 | 175 | 1 | 100 | grapeshot | semi_automatic |
| handcannon | shotgun | 15.5 | 1 | 6 | 51.7 | 12.0 | 140 | 1 | 45 | grapeshot | semi_automatic |
| ribs_glory | shotgun | 28.5 | 1 | 12 | 47.5 | 4.5 | 140 | 8 | 20 | bearpack_shell | semi_automatic |
| inertial | shotgun | 26.5 | 1 | 13 | 40.8 | 6.6 | 140 | 6 | 30 | bearpack_shell | semi_automatic |
| brawler | shotgun | 25.5 | 1 | 13 | 39.2 | 12.25 | 140 | 1 | 20 | bearpack_shell | semi_automatic |
| shellurker | shotgun | 19.0 | 1 | 10 | 38.0 | 3.0 | 90 | 14 | 30 | shulkshot | automatic |
| echoes_2 | shotgun | 22.0 | 1 | 13 | 33.8 | 6.0 | 550 | 4 | 15 | sculk_cell | semi_automatic |
| mangalitsa | shotgun | 22.0 | 1 | 13 | 33.8 | 5.5 | 200 | 7 | 30 | hog_round | semi_automatic |
| combat_shotgun | shotgun | 21.0 | 1 | 15 | 28.0 | 12.0 | 200 | 8 | 30 | shotgun_shell | semi_automatic |
| floundergat | shotgun | 15.0 | 1 | 11 | 27.3 | 10.8 | 190 | 4 | 30 | grapeshot | semi_automatic |
| venturi | shotgun | 15.5 | 1 | 12 | 25.8 | 7.5 | 200 | 8 | 15 | buckshot | pulse |
| doublet | shotgun | 16.0 | 1 | 13 | 24.6 | 12.0 | 175 | 2 | 100 | grapeshot | semi_automatic |
| homemaker | shotgun | 17.5 | 1 | 17 | 20.6 | 5.0 | 200 | 5 | 20 | shatter_round | semi_automatic |
| killer_23 | shotgun | 17.5 | 1 | 17 | 20.6 | 5.0 | 200 | 5 | 20 | shotgun_shell | semi_automatic |
| callwell_conversion | shotgun | 15.0 | 1 | 15 | 20.0 | 9.5 | 160 | 6 | 20 | shotgun_shell | semi_automatic |
| callwell_terminal | shotgun | 16.0 | 1 | 16 | 20.0 | 6.5 | 200 | 6 | 20 | shotgun_shell | semi_automatic |
| rat_king_and_queen | smg | 23.0 | 1 | 3 | 153.3 | 2.0 | 360 | 20 | 20 | ramrod_round | automatic |
| prush_gun | smg | 7.0 | 1 | 2 | 70.0 | 2.0 | 150 | 23 | 35 | compact_advanced_round | automatic |
| freyr | smg | 9.5 | 1 | 3 | 63.3 | 3.0 | 120 | 24 | 35 | hog_round | automatic |
| soul_drummer | smg | 6.0 | 1 | 2 | 60.0 | 4.0 | 315 | 25 | 20 | compact_advanced_round | automatic |
| greaser_smg | smg | 7.5 | 1 | 3 | 50.0 | 4.0 | 140 | 18 | 100 | compact_advanced_round | automatic |
| mak_mkii | smg | 4.5 | 1 | 2 | 45.0 | 3.0 | 315 | 19 | 50 | compact_copper_round | automatic |
| carapice | smg | 13.0 | 1 | 6 | 43.3 | 3.0 | 60 | 15 | 30 | shulkshot | automatic |
| rusty_gnat | smg | 5.0 | 1 | 3 | 33.3 | 3.25 | 175 | 14 | 35 | compact_copper_round | automatic |
| nailer | smg | 4.5 | 1 | 3 | 30.0 | 4 | 112 | 28 | 20 | needle | automatic |
| whispers | smg | 11.0 | 1 | 8 | 27.5 | 2.0 | 455 | 12 | 35 | sculk_cell | automatic |
| repeating_musket | smg | 7.5 | 1 | 13 | 11.5 | 3.0 | 150 | 5 | 60 | powder_and_ball | semi_automatic |
| stiletto | sniper | 22.5 | 1 | 5 | 90.0 | 1.0 | 300 | 1 | 40 | krahg_round | semi_automatic |
| big_bore | sniper | 74.0 | 1 | 20 | 74.0 | 0.0 | 1400 | 1 | 70 | osborne_slug | semi_automatic |
| saketini | sniper | 14.5 | 1 | 6 | 48.3 | 0.5 | 385 | 1 | 70 | standard_copper_round | semi_automatic |
| mas_peddler | sniper | 16.5 | 1 | 8 | 41.2 | 0.5 | 720 | 12 | 30 | ramrod_round | semi_automatic |
| pyroclastic_flow | sniper | 15.0 | 1 | 8 | 37.5 | 3.0 | 630 | 7 | 20 | blaze_fuel | pulse |
| astella | sniper | 44.0 | 1 | 24 | 36.7 | 0.0 | 1400 | 7 | 30 | krahg_round | semi_automatic |
| mas_55 | sniper | 19.5 | 1 | 11 | 35.5 | 0.5 | 900 | 8 | 30 | gibbs_round | semi_automatic |
| whistler | sniper | 19 | 1 | 12 | 31.7 | 1 | 500 | 1 | 40 | gibbs_round | semi_automatic |
| howler | sniper | 26.0 | 1 | 17 | 30.6 | 0.0 | 910 | 6 | 30 | krahg_round | semi_automatic |
| trotters | sniper | 18.0 | 1 | 16 | 22.5 | 1.0 | 330 | 7 | 40 | hog_round | semi_automatic |
| iron_spear | sniper | 18.5 | 1 | 22 | 16.8 | 1.0 | 600 | 7 | 40 | gibbs_round | semi_automatic |
| turnpike | sniper | 14.0 | 1 | 17 | 16.5 | 1.0 | 750 | 6 | 40 | standard_copper_round | semi_automatic |
| forlorn_hope | sniper | 17.0 | 1 | 21 | 16.2 | 2.0 | 585 | 5 | 35 | sculk_cell | semi_automatic |
| saketini_ironport | sniper | 14.0 | 1 | 27 | 10.4 | 0.5 | 385 | 6 | 70 | standard_copper_round | semi_automatic |
| bomb_lance | special | 17.5 | 1 | 6 | 58.3 | 1.0 | 135 | 1 | 40 | microjet | semi_automatic |
| hullbreaker | special | 23.0 | 1 | 8 | 57.5 | 5.0 | 270 | 1 | 60 | shotball | pulse |
| niami | special | 12.0 | 1 | 13 | 18.5 | 1.0 | 260 | 8 | 30 | minecraft:arrow | semi_automatic |
| crusader | special | 0.0 | 1 | 19 | 0.0 | 1.0 | 80 | 4 | 30 | syringe | semi_automatic |
