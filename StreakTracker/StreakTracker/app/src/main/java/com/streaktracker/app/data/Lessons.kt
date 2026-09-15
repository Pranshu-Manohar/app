package com.streaktracker.app.data

/** Deterministic-by-date short lessons. Never shaming — always encouraging return after a miss. */
object Lessons {
    val ALL = listOf
        (
        "Consistency is not about never failing. It is about returning after failure.",
        "A single missed day doesn't erase your progress. It's just one data point.",
        "Momentum matters more than perfection. Keep moving, even slowly.",
        "The goal isn't to feel motivated. It's to act even when you don't.",
        "What you repeat, you become. Choose your repetitions carefully.",
        "Rest is part of the process, but returning is what makes it count.",
        "You don't need a perfect record. You need a resilient one.",
        "Small, boring, repeated actions build big, exciting results.",
        "The best time to restart was yesterday. The next best time is today.",
        "Growth happens in the moments you choose discipline over comfort.",
        "One day does not define you. Your pattern over time does.",
        "Every expert was once inconsistent before they became consistent.",
        "Progress isn't a straight line. Missed days are just bends in the road.",
        "The habit you're building matters more on hard days than on easy ones.",
        "Judge yourself by whether you return, not by whether you never fall.",
        "Identity is built by evidence. Each completed day is evidence of who you are.",
        "You are always one action away from getting back on track.",
        "Don't count the days you missed. Count the days you returned.",
        "Effort compounds quietly before it pays off loudly.",
        "The comeback is always part of the story, not a failure of it.",
        "Discomfort today is often just the shape of growth.",
        "You're not behind. You're exactly where consistent effort has taken you.",
        "A streak is a tool for motivation, not a measure of your worth.",
        "Some days will be about starting. Other days will be about not stopping.",
        "The days you don't feel like it are the days that matter most.",
        "Real discipline is quiet. It doesn't need an audience to keep showing up.",
        "Every rebuild makes the foundation stronger than before.",
        "You can't control every day, but you can control your next choice.",
        "The version of you a year from now is built by today's small choice.",
        "Setbacks are information, not verdicts.",
        "Trust the process more on the days it's hard to see results.",
        "Consistency beats intensity when it's measured in years, not days.",
        "It's okay to miss a step. It's not okay to quit walking.",
        "The strongest habits survive bad days without breaking entirely.",
        "You're allowed to be a work in progress and proud of your progress.",
        "Discipline is a muscle. Every rep, even after a miss, makes it stronger.",
        "The story isn't over because of one missed chapter.",
        "Focus on the next 24 hours. That's the only streak you can control.",
        "Compassion for a missed day fuels tomorrow more than guilt does.",
        "Show up imperfectly rather than not at all."
    )

    fun forDate(epochDay: Long): String = ALL[(epochDay.mod(ALL.size.toLong())).toInt()]
}
