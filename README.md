# Later Ladder - Customized AFL team standings and game tracker

<p align="center">
    <picture>
        <source media="(prefers-color-scheme: light)" srcset="vue/src/assets/LaterLadder-Logo-SocialLight.webp" width="468">
        <source media="(prefers-color-scheme: dark)" srcset="vue/src/assets/LaterLadder-Logo-SocialDark.webp" width="468">
        <img alt="Later Ladder wordmark" src="vue/src/assets/LaterLadder-Logo-SocialLight.webp">
    </picture>
</p>

<h3 align="center">
    Later Ladder can now be viewed at <a href="https://www.laterladder.com">LaterLadder.com</a>.
</h3>

## About Later Ladder

As an American fan of the Australian Football League (AFL) and Aussie rules football, one of the challenges I've faced is the inconvenience of checking current team standings without inadvertently spoiling the outcome of matches that occurred overnight.

Later Ladder offers a solution: users can select which games they've watched, and the team ladder will update only according to the results of watched games. This unique feature ensures that you can stay informed about the current AFL standings while preserving the excitement of each match!

![Later Ladder demonstration](demo.gif)

## Status

**Later Ladder is a work in progress. It is a passion project that will continue to evolve over time as I expand both the project scope and my abilities as a developer.**

Core functionality is now complete. Users are able mark games as watched to update the team ladder. Registered users can save their watched games list.

### Recent updates
- Guests can now use Later Ladder without registering for an account
- Final scores now appear once a game is marked as watched
- Games are updated in real time using server-sent events. Once a game has finished, it can immediately be marked as watched.

### Planned features:
- Mark all games watched/unwatched up to present
- Ability to toggle visibility of game scores
- Toggle for light and dark modes
