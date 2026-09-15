package com.streaktracker.app.data

/** Deterministic-by-date quote pool: discipline, consistency, patience, persistence, progress. */
object Quotes {
    val ALL = listOf(
        "Discipline is choosing between what you want now and what you want most.",
        "Consistency is what transforms average into excellence.",
        "Small daily improvements are the key to staggering long-term results.",
        "You don't have to be great to start, but you have to start to be great.",
        "Patience is not passive waiting. It is active building, one day at a time.",
        "Every day you show up is a vote for the person you're becoming.",
        "Motivation gets you started. Discipline keeps you going.",
        "The pain of discipline weighs ounces; the pain of regret weighs tons.",
        "Progress is progress, no matter how small it looks today.",
        "Self-control is strength. Right thought is mastery. Calmness is power.",
        "A river cuts through rock not because of its power, but its persistence.",
        "You will never change your life until you change something you do daily.",
        "Success is the sum of small efforts, repeated day in and day out.",
        "Fall down seven times, stand up eight.",
        "The secret of getting ahead is getting started, again, every single day.",
        "Long-term consistency trumps short-term intensity.",
        "What you do today can improve all your tomorrows.",
        "It's not that I'm so smart. I just stay with problems longer.",
        "Great things are done by a series of small things brought together.",
        "The chains of habit are too weak to be felt until they are too strong to be broken.",
        "Failure is not the opposite of success; it is part of success.",
        "The only way to build a habit is to keep the promise you made to yourself.",
        "Do not wait for the perfect moment. Take the moment and make it perfect.",
        "One day or day one. You decide.",
        "Habits are the compound interest of self-improvement.",
        "You are not your best day, and you are not your worst day. You are your average.",
        "The expert in anything was once a beginner who refused to quit.",
        "Slow progress is still progress. Standing still is not.",
        "It always seems impossible until it is done.",
        "Discipline is the bridge between goals and accomplishment.",
        "Show up for yourself today, even in the smallest way.",
        "Persistence guarantees that results are inevitable.",
        "A goal without daily action is just a wish.",
        "You can't build a reputation on what you're going to do tomorrow.",
        "The struggle you're in today is developing the strength you need for tomorrow.",
        "Character is built by what you do consistently, not occasionally.",
        "Winners are just losers who tried one more time.",
        "There is no elevator to success. You have to take the stairs, one day at a time.",
        "Every action you take is a vote for the type of person you wish to become.",
        "Time and patience change the mulberry leaf to satin."
    )

    fun forDate(epochDay: Long): String = ALL[(epochDay.mod(ALL.size.toLong())).toInt()]
}
