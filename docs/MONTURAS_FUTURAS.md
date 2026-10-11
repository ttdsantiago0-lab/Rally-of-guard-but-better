# MONTURAS_FUTURAS — osos, elefantes, hipogrifos y, mucho más adelante, dragones (IDEA ESTACIONADA, 2026-10-11)

**Estado:** apuntado para no olvidarlo. No entra antes de E4. Hoy el sistema de montura (`MountDriver`, `GuardMounts`) está pensado para caballos; el estudio de armas de asedio y monturas está en `INVESTIGACION_CBC_MONTURAS.md`.

## Lo que existe y lo que se sabe (verificado en el jar instalado, sin ejecutar)
- **Ice and Fire** (`iceandfire-2.0-beta.17`): hay `HippogryphEntity` y `DragonBaseEntity` (más `AmphithereEntity`). Esto prueba que existen como clases; **no** se ha verificado cómo se montan ni cómo exponen su IA/vuelo.
- **Alex's Mobs** (osos, elefantes): en esta sesión no pude abrir su jar; hay que verificar qué entidades existen y si admiten jinete.
- Para animales de carga ya se previó en `LOGISTICA_Y_EQUIPO.md` §7 (elefantes como depósito del campamento si exponen inventario).

## Ruta propuesta (de menos a más riesgo)
1. **Monturas terrestres adicionales** (oso, elefante): mismo conductor que el caballo con un `MountProfile` (velocidad, aguante, capacidad de jinetes, desplazamiento del jinete, papel: carga, escolta, carga pesada).
2. **Monturas aéreas** (hipogrifo): requiere navegación 3D, aterrizaje/despegue, línea de vista distinta y **nuevas capacidades** (`AIR_SUPPORT`, `RECON` aéreo). Cambia el `canReach` de amenazas (E3C T1).
3. **Dragones (mucho más adelante):** unidad de leyenda; exige rango mínimo, vínculo con la montura, estilos de mando propios y reglas de equilibrio fuertes (daño de área, aliento). Riesgo alto de rendimiento y de IA.

## Preguntas a resolver antes
- ¿Cómo monta/dirige a cada una el mod original (teclas de jugador, órdenes de domar)? ¿Puede la IA de un guardia imitarlo?
- ¿Cómo se integran con el `Doctrine` y con las firmas (arco desde hipogrifo)?
- Un `MountProfile` genérico por JSON para añadir monturas de otros mods sin tocar código, como se hizo con la maestría y las firmas.
