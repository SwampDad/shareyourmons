# How ShareYourMons Recognizes Cobblemon Town Skins

When I started working on ShareYourMons, the goal was pretty simple: make it easier to see what was in my Cobblemon PC and share the interesting parts of my collection.

I wanted to find my custom skins, shinies, and rarer Pokémon without opening every box and checking them one at a time.

The basic collection viewer was one part of the problem. Figuring out which Pokémon actually had custom skins turned into a separate investigation.

## The problem with skin names

Players recognize skins by names like Neon, Spooky, and Adventure.

The game doesn’t necessarily store those names on the Pokémon.

Instead, Cobblemon exposes information such as the Pokémon’s species, form, and a set of internal appearance markers called aspects.

An aspect might describe a special skin. It might also describe something completely ordinary, like a regional form or another appearance variation.

That means finding an unusual aspect isn’t enough to call something a custom skin.

I needed to work out which markers represented actual Cobblemon Town skin collections, which ones meant something else, and which ones were still unknown.

## Starting with what the community knew

The first useful information came from the community.

Public announcements and discussions helped establish the names of skin collections, when they appeared, and how players referred to them.

That was a good starting point, but it wasn’t a complete answer.

Players sometimes used different names for the same collection. Some collections had more documentation than others. And knowing that a skin existed didn’t necessarily tell me which internal marker the game used for it.

The Cobblemon Town wiki helped fill in more of that picture.

I gathered the available collection pages and organized their information so I could compare names, Pokémon, and collection membership consistently.

That process covered 75 captured wiki pages and 491 collection entries.

The important part wasn’t just collecting the pages. It was making sure I could tell the difference between what a source actually said and what my processing had interpreted.

## Neon, Tron, and a name that didn’t match

One of the clearest examples was Neon.

Early community research made it look like Neon and Tron might be separate skin collections.

Then I started comparing that information with observed game data.

The internal marker I found was tron2026.

That name alone could have reinforced the idea that Tron was its own collection. But the Pokémon carrying the marker told a different story.

All 25 observed species with that marker matched the published Neon roster.

That gave me a much stronger reason to connect tron2026 to Neon than the marker’s spelling did.

ShareYourMons now recognizes Neon as the collection name and treats Tron as an alternate name, not a separate family.

It was an early reminder that I couldn’t build reliable classifications by guessing what internal names meant.

## Looking at what the game actually showed

The next step was comparing documentation with observed data.

Research snapshots included information from trading listings, PC collections, and shop records. Together, those sources exposed appearance markers that weren’t always explained in the wiki.

They also revealed gaps.

Some records didn’t preserve every appearance marker. Some markers appeared on Pokémon that weren’t listed in the available collection pages. And some unusual-looking markers turned out to represent ordinary forms rather than special skins.

Instead of treating every mismatch as an error in one source, I kept the differences visible until there was enough evidence to explain them.

That approach became important when a Pokémon I expected to see didn’t appear in the report.

## The Adventure Zoroark problem

During testing, an Adventure Zoroark was missing from the default report.

The Pokémon wasn’t necessarily missing from the PC. The problem was that the report didn’t yet recognize its skin marker, and the default filters were focused on special categories.

I went back through the available evidence.

Public staff announcements described the Adventure collection, including an Adventure Ranked Zoroark. The server resource-pack definitions also showed Zoroark variants using adventure2026, sometimes alongside another marker, ranked.

Those markers meant different things.

adventure2026 identified the skin collection. ranked was an additional variation, not a separate custom-skin family.

That investigation led to two changes in the classification approach: recognize Adventure as a confirmed family, and make sure unfamiliar appearance markers are still reported even when the default filters don’t show the Pokémon itself.

I did not capture a fresh, complete list of the live Zoroark’s aspects during that investigation, so I don’t treat the example as proof of every possible Adventure Zoroark combination.

It did, however, expose a real weakness in how the report handled unfamiliar information.

## One misspelled Pokémon, two different identities

Another problem came from the research data rather than the game.

One wiki heading spelled Serperior as Seperior.

That one-letter difference was enough for an early processing step to treat the name as a separate Pokémon.

The result was a made-up species entry and a missing connection between the real Serperior and the Spooky collection.

The surrounding evidence pointed to the intended Pokémon: the page’s image filenames used the correct spelling, and Cobblemon’s own species data did too.

The fix wasn’t to silently rewrite the original source.

I kept the captured spelling as part of the evidence and added an explicit correction when matching the name to the game’s species data.

That distinction matters. If I change the source itself, it’s harder to tell later whether a difference came from the original material or from my processing.

A small spelling issue ended up being a useful lesson in keeping source evidence separate from the cleaned-up information the mod needs.

## When 39 skins were really 30

The biggest classification lesson came from an early version of the skin resolver.

At first, it treated some promising but unconfirmed appearance markers as skins.

That sounded reasonable. If a marker looked unusual and had appeared during skin research, it seemed worth including.

But the result was wrong.

In one historical collection snapshot, the early resolver counted 39 skin instances. Review showed that nine of those were ordinary Archelian forms, leaving 30 recognized skin instances.

The issue wasn’t that those Pokémon were uninteresting. They just weren’t part of the custom-skin category I was trying to report.

That changed the rule I wanted the mod to follow:

A marker isn’t a confirmed skin just because it looks like one.

The current ShareYourMons catalog uses a reviewed list of exact skin markers. Known non-skin markers are excluded from skin classification, and unfamiliar ones remain unresolved.

The 39-versus-30 result was an early research and prototype issue, not a defect in the current release.

## What the mod does now

The classification rules are deliberately cautious.

When ShareYourMons reads a Pokémon’s appearance markers, it checks them against the reviewed catalog.

If it finds one confirmed skin marker, it can identify the collection.

If it finds a known non-skin marker, it doesn’t mistake that for a custom skin.

If it finds an unfamiliar marker, it preserves that information for review.

If more than one confirmed skin marker appears together, it reports a conflict instead of choosing one.

Legendary, Mythical, Paradox, and Ultra Beast categories are handled separately. Those categories come from the game’s species information, not from whether a Pokémon has a skin.

This doesn’t mean the catalog will always be complete. It means the report should be clear about the difference between what it recognizes and what it doesn’t.

## Checking the results

I compared the classification data against the available source material, observed examples, and test cases.

The current public catalog contains ten recognized skin families and 54 known non-skin appearance markers. Nine of those families form the owner-accepted historical progression; Easter is a separately evidenced event offering. The catalog also includes the species information needed for the mod’s other collection categories.

The project has an automated test suite covering classification, filtering, report formatting, and related behavior.

Those tests help catch regressions in the code. They don’t prove that every skin ever released on the server has been documented.

That still depends on the quality and coverage of the underlying evidence.

## What I still don’t know

Cobblemon Town can add or change content.

Public documentation may not cover every special reward or variation. Observed records only show what was available in the data I examined. A resource-pack definition doesn’t prove that a particular Pokémon is currently obtainable.

And a player’s PC snapshot only represents the information available to that client at that time.

So ShareYourMons is not an official skin registry, an availability tracker, or a live view of the server.

It’s a collection tool built around the best reviewed information I could establish during development.

## Why I kept the uncertain parts

The part of this project I found most useful wasn’t simply ending up with a list of skins.

It was learning where the information came from, finding where sources disagreed, and deciding what counted as enough evidence to make a claim.

Neon showed why names can be misleading.

Adventure Zoroark showed how an unfamiliar marker could hide a real result.

Serperior showed why source text and corrected identities should be kept separate.

And the 39-versus-30 mistake showed what happens when a tool starts treating possibilities as facts.

Those decisions are now part of how ShareYourMons works.

If a future skin appears with a marker I haven’t reviewed, I’d rather have the mod tell me “I don’t know what this is yet” than confidently give it the wrong name.

---

This research drew on publicly available Cobblemon Town wiki pages and announcements, observed gameplay data, Cobblemon 1.8.2 species information, and reviewed development evidence. Private player records, raw community messages, and third-party game assets are not included in this repository.

For the implementation details, see the [classification reference](../classification.md). For installation and use, return to the [README](../../README.md).
