package underdocs.cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.subcommands
import underdocs.cli.commands.parse.ParseCommand
import underdocs.cli.commands.render.RenderCommand

class Underdocs : CliktCommand(
        help = "Underdocs"
) {
    override fun run() = Unit
}

fun main(args: Array<String>) = Underdocs()
        .subcommands(ParseCommand(), RenderCommand())
        .main(args)
