/*
 * This file is part of lanterna (http://code.google.com/p/lanterna/).
 * 
 * lanterna is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Copyright (C) 2010-2015 Martin
 */
package com.googlecode.lanterna.terminal.ansi;

import com.googlecode.lanterna.TerminalSize;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class extends UnixLikeTerminal and implements the Cygwin-specific implementations. This means, running a Java
 * application using Lanterna inside the Cygwin Terminal application. The standard Windows command prompt (cmd.exe) is
 * not supported by this class.
 *
 * @author Martin
 * @author Andreas
 */
public class CygwinTerminal extends UnixLikeTerminal {

    private static final Pattern STTY_SIZE_PATTERN = Pattern.compile(".*rows ([0-9]+);.*columns ([0-9]+);.*");
    private static final String STTY_LOCATION = findProgram("stty.exe");

    public CygwinTerminal(
            InputStream terminalInput,
            OutputStream terminalOutput,
            Charset terminalCharset) throws IOException {
        super(terminalInput, terminalOutput, terminalCharset,
              CtrlCBehaviour.TRAP, null);

        //Make sure to set an initial size
        onResized(80, 24);

        saveSTTY();
        setCBreak(true);
        setEcho(false);
        sttyMinimum1CharacterForRead();
        setupShutdownHook();
    }

    @Override
    public TerminalSize getTerminalSize() {
        try {
            String stty = exec(findSTTY(), "-F", getPseudoTerminalDevice(), "-a");
            Matcher matcher = STTY_SIZE_PATTERN.matcher(stty);
            if(matcher.matches()) {
                return new TerminalSize(Integer.parseInt(matcher.group(2)), Integer.parseInt(matcher.group(1)));
            }
            else {
                return new TerminalSize(80, 24);
            }
        }
        catch(Throwable e) {
            return new TerminalSize(80, 24);
        }
    }

    @Override
    protected void sttyKeyEcho(final boolean enable) throws IOException {
        runSTTYCommand(enable ? "echo" : "-echo");
    }

    @Override
    protected void sttyMinimum1CharacterForRead() throws IOException {
        runSTTYCommand("min", "1");
    }

    @Override
    protected void sttyICanon(final boolean enable) throws IOException {
        runSTTYCommand(enable ? "icanon" : "cbreak");
    }

    @Override
    protected String sttySave() throws IOException {
        return runSTTYCommand("-g").trim();
    }

    @Override
    protected void sttyRestore(String tok) throws IOException {
        runSTTYCommand(tok);
    }

    protected String findSTTY() {
        return STTY_LOCATION;
    }

    private String runSTTYCommand(String... parameters) throws IOException {
        List<String> commandLine = new ArrayList<String>(Arrays.asList(
                findSTTY(),
                "-F",
                getPseudoTerminalDevice()));
        commandLine.addAll(Arrays.asList(parameters));
        return exec(commandLine.toArray(new String[commandLine.size()]));
    }

    private String getPseudoTerminalDevice() {
        //This will only work if you only have one terminal window open, otherwise we'll need to figure out somehow
        //which pty to use, which could be very tricky...
        return "/dev/pty0";
    }

    private static String findProgram(String programName) {
        String[] paths = System.getProperty("java.library.path").split(";");
        for(String path : paths) {
            File shBin = new File(path, programName);
            if(shBin.exists()) {
                return shBin.getAbsolutePath();
            }
        }
        return programName;
    }

}
