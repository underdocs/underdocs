const { execSync } = require('child_process')

const branch = require('./lib/branch')

const OWNER = 'underdocs'
const REPOSITORY = 'underdocs';

(async function main () {
  const issueArgument = process.argv[2]
  const indexArgument = process.argv[3]

  let issueNumber
  try {
    issueNumber = Number.parseInt(issueArgument)
  } catch {
    console.log(`The issue number provided "${issueArgument}" is not a valid number!\n\n  Usage: npm generate:branch -- 17`)
    process.exit(1)
  }

  let index
  if (indexArgument !== undefined) {
    try {
      index = Number.parseInt(indexArgument)
    } catch {
      console.log(`The index provided "${indexArgument}" is not a valid number!\n\n  Usage: npm generate:branch -- 17 2`)
      process.exit(1)
    }
  }

  const branchName = await branch.branchNameForIssue(OWNER, REPOSITORY, issueNumber, index)

  execSync(`git checkout -b "${branchName}"`)
})()
